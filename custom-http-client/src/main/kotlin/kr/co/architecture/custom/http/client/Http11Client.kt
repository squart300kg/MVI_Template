package kr.co.architecture.custom.http.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.ACCEPT
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.ACCEPT_ENCODING
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CONNECTION
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.HOST
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.USER_AGENT
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.GZIP
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.IMAGE_ALL
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.KEEP_ALIVE
import kr.co.architecture.custom.http.client.interceptor.CustomHttpLogger
import kr.co.architecture.custom.http.client.model.Bytes
import kr.co.architecture.custom.http.client.model.toBytes
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

data class HttpResponse(
  val code: Int,
  val message: String,
  val header: Map<String, String> = emptyMap(),
  val body: Bytes = Bytes(byteArrayOf())
)

class RawHttp11Client private constructor(
  private val userAgent: String,
  private val readTimeoutMs: Int,
  private val maxRedirects: Int,
  private val httpLogger: CustomHttpLogger?
) {

  companion object {
    @Volatile
    private var INSTANCE: RawHttp11Client? = null

    @JvmStatic
    fun getInstance(
      userAgent: String = "RawHttp11/0.1",
      readTimeoutMs: Int = 10_000,
      maxRedirects: Int = 5,
      httpLogger: CustomHttpLogger? = null
    ): RawHttp11Client {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: RawHttp11Client(
          userAgent = userAgent,
          readTimeoutMs = readTimeoutMs,
          maxRedirects = maxRedirects,
          httpLogger = httpLogger,
        ).also { INSTANCE = it }
      }
    }
  }

  suspend fun callApi(
    method: String,
    url: String,
    headers: Map<String, String> = emptyMap(),
    onResponseSuccess: suspend HttpResponse.() -> Unit = {},
    onResponseError: suspend HttpResponse.() -> Unit = {},
    onResponseException: suspend Throwable.() -> Unit = {},
  ) = request(
    method = method,
    url = URL(url),
    body = null,
    extraHeaders = headers,
    redirectDepth = 0,
    onResponseSuccess = onResponseSuccess,
    onResponseError = onResponseError,
    onResponseException = onResponseException,
  )

  private suspend fun request(
    method: String,
    url: URL,
    body: ByteArray?,
    extraHeaders: Map<String, String> = emptyMap(),
    redirectDepth: Int,
    onResponseSuccess: suspend (HttpResponse) -> Unit = {},
    onResponseError: suspend (HttpResponse) -> Unit = {},
    onResponseException: suspend (Throwable) -> Unit = {}
  ) {
    withContext(Dispatchers.IO) {
      try {
        require(redirectDepth <= maxRedirects) { "redirects count is max($maxRedirects)" }
        val startNs = System.nanoTime()

        val host = url.host
        val pathAndQuery = url.buildPathAndQuery()
        val socket = getSocket(url = url, readTimeoutMs = readTimeoutMs)

        socket.use { s ->
          val bufferedOutputStream = BufferedOutputStream(s.getOutputStream())
          val bufferedInputStream = BufferedInputStream(s.getInputStream())

          // ---- 요청 라인 + 헤더 ----
          // TODO: 왜 LinkedMap을 썼는지?
          //  단순 StringBuilder로
          val requestHeader = linkedMapOf(
            HOST to host,
            USER_AGENT to userAgent,
            ACCEPT to IMAGE_ALL,
            ACCEPT_ENCODING to GZIP,
            CONNECTION to KEEP_ALIVE
          ).apply {
            if (body != null) put(HttpHeaderConstants.Property.CONTENT_LENGTH, "${body.size}")
            extraHeaders.forEach { (k, v) -> putIfAbsent(k, v) }
          }

          httpLogger?.printRequestStartLog(method, "$url", HttpHeaderConstants.HTTP_1_1)
          httpLogger?.printRequestHeaderLog(requestHeader)
          httpLogger?.printRequestBodyLog()

          val requestHeaderString = buildString {
            append("$method $pathAndQuery ${HttpHeaderConstants.HTTP_1_1}\r\n")
            requestHeader.forEach { (k, v) -> append("$k: $v\r\n") }
            append("\r\n")
          }
          bufferedOutputStream.run {
            write(requestHeaderString.toByteArray(Charsets.US_ASCII))
            if (body != null) write(body)
            flush()
          }

          // ---- 상태줄 ----
          val statusLine =
            readLineAscii(bufferedInputStream) ?: throw IOException("abnormal status line")
          val statusParts = statusLine.split(' ', limit = 3)
          val code = statusParts.getOrNull(1)?.toIntOrNull()
            ?: throw IOException("http status code is not number: $statusLine")
          val message = statusParts.getOrNull(2) ?: ""

          // ---- 헤더 ----
          val responseHeader = mutableMapOf<String, String>()
          while (true) {
            val line = readLineAscii(bufferedInputStream) ?: break
            if (line.isEmpty()) break
            val splitIndex = line.indexOf(':')
            if (splitIndex > 0) {
              val property = line.substring(0, splitIndex).trim().lowercase(Locale.ROOT)
              val value = line.substring(splitIndex + 1).trim()
              responseHeader[property] = value
            }
          }

          val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
          httpLogger?.printResponseStartLog(code, message, "$url", tookMs)
          httpLogger?.printResponseHeaderLog(responseHeader)

          // ---- 리다이렉트 ----
          // TODO: 30x응답이 이렇게 많이 오는게 확실한가?
          //  302만 오는게 아닌지 체크
          if (code in listOf(301, 302, 303, 307, 308)) {
            val location = responseHeader[HttpHeaderConstants.Property.LOCATION]
              ?: throw IOException("Redirect without Location")
            val redirectUrl = URL(url, location)
            return@withContext request(
              method = if (code == 303) HttpHeaderConstants.Method.GET else method,
              url = redirectUrl,
              body = if (code >= 307) body else null,
              extraHeaders = extraHeaders,
              redirectDepth = redirectDepth + 1,
              onResponseSuccess = onResponseSuccess,
              onResponseError = onResponseError,
              onResponseException = onResponseException
            )
          }

          // ---- 바디 ----
          val transfer = responseHeader[HttpHeaderConstants.Property.TRANSFER_ENCODING]
          val contentLen =
            responseHeader[HttpHeaderConstants.Property.CONTENT_LENGTH]?.toLongOrNull()
          val rawBody = when {
            code == 304 -> ByteArray(0) // 조건부 요청 성공(본문 없음)
            transfer?.contains(HttpHeaderConstants.Value.CHUNKED) == true -> readChunked(
              bufferedInputStream
            )
            contentLen != null -> readFixed(bufferedInputStream, contentLen)
            else -> readToEnd(bufferedInputStream)
          }

          if (code in 400..599) {
            onResponseError(
              HttpResponse(
                code = code,
                message = message,
                body = rawBody.toBytes()
              )
            )
            return@withContext
          }

          // ---- gzip 해제 ----
          val contentType = responseHeader[HttpHeaderConstants.Property.CONTENT_TYPE]
          val isGzip = responseHeader[HttpHeaderConstants.Property.CONTENT_ENCODING]?.contains(
            HttpHeaderConstants.Value.GZIP
          ) == true
          val bodyBytes =
            if (code != 304 && isGzip) GZIPInputStream(ByteArrayInputStream(rawBody)).use { it.readBytes() }
            else rawBody

          httpLogger?.printResponseBodyLog(
            body = bodyBytes,
            contentType = contentType,
            wasGzip = isGzip,
            rawSize = if (isGzip) rawBody.size else null
          )

          onResponseSuccess(
            HttpResponse(
              code = code,
              message = message,
              header = responseHeader,
              body = bodyBytes.toBytes()
            )
          )
        }
      } catch (e: Exception) {
        onResponseException(e)
      }
    }
  }
}