package kr.co.architecture.custom.http.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.custom.http.client.HttpHeaderConstants.HTTP_1_1
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.ACCEPT
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.ACCEPT_ENCODING
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CONNECTION
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CONTENT_ENCODING
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CONTENT_LENGTH
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CONTENT_TYPE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.HOST
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.LOCATION
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.TRANSFER_ENCODING
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.USER_AGENT
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.CHUNKED
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.GZIP
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.IMAGE_ALL
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.KEEP_ALIVE
import kr.co.architecture.custom.http.client.HttpStatusCode.MOVED_TEMP
import kr.co.architecture.custom.http.client.HttpStatusCode.NOT_MODIFIED
import kr.co.architecture.custom.http.client.interceptor.CustomHttpLogger
import kr.co.architecture.custom.http.client.model.HttpResponse
import kr.co.architecture.custom.http.client.model.toBytes
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

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
    body: ByteArray? = null,
    onResponseSuccess: suspend HttpResponse.() -> Unit = {},
    onResponseError: suspend HttpResponse.() -> Unit = {},
    onResponseException: suspend Throwable.() -> Unit = {},
  ) = callApi(
    method = method,
    url = URL(url),
    body = body,
    extraHeaders = headers,
    redirectDepth = 0,
    onResponseSuccess = onResponseSuccess,
    onResponseError = onResponseError,
    onResponseException = onResponseException,
  )

  private suspend fun callApi(
    method: String,
    url: URL,
    body: ByteArray? = null,
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

        // ---- hand shake 및 연결 ----
        val host = url.host
        val pathAndQuery = url.extractPathAndQuery()
        val socket = getSocket(
          url = url,
          readTimeoutMs = readTimeoutMs
        )

        socket.use { socket ->
          val bufferedOutputStream = BufferedOutputStream(socket.getOutputStream())
          val bufferedInputStream = BufferedInputStream(socket.getInputStream())

          // ---- 요청 헤더 ----
          val requestHeader = buildString {
            append("$method $pathAndQuery ${HTTP_1_1}\r\n")
            append("$HOST: $host\r\n")
            append("$USER_AGENT: $userAgent\r\n")
            append("$ACCEPT: $IMAGE_ALL\r\n")
            append("$ACCEPT_ENCODING: $GZIP\r\n")
            append("$CONNECTION: $KEEP_ALIVE\r\n")
            if (body != null) append("$CONTENT_LENGTH: ${body.size}")
            extraHeaders.forEach { (k, v) -> append("$k: $v") }
            append("\r\n")
          }
          bufferedOutputStream.run {
            write(requestHeader.toByteArray(Charsets.US_ASCII))
            if (body != null) write(body)
            flush()
          }
          httpLogger?.printRequestStartLog(method, "$url", HTTP_1_1)
          httpLogger?.printRequestHeaderLog(requestHeader)
          httpLogger?.printRequestBodyLog()

          // ---- 응답 헤더 상태줄 파싱 (HTTP/1.1 200 OK) ----
          val (httpStatusCode, httpStatusMessage) = run {
            readLineAscii(bufferedInputStream)?.let { statusLine ->
              statusLine.split(' ', limit = 3).run {
                val code = getOrNull(1)?.toIntOrNull() ?: throw IOException("http status code is abnormal: $statusLine")
                val message = getOrNull(2) ?: throw IOException("http status message is abnormal: $statusLine")
                code to message
              }
            } ?: throw IOException("http status line is null")
          }

          // ---- 응답 헤더 본문 파싱 ----
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
          httpLogger?.printResponseStartLog(httpStatusCode, httpStatusMessage, "$url", tookMs)
          httpLogger?.printResponseHeaderLog(responseHeader)

          // ---- 리다이렉트 진행 ----
          if (httpStatusCode == MOVED_TEMP) {
            val location = responseHeader[LOCATION]
              ?: throw IOException("Redirect without Location")
            return@withContext callApi(
              method = method,
              url = URL(url, location),
              extraHeaders = extraHeaders,
              redirectDepth = redirectDepth + 1,
              onResponseSuccess = onResponseSuccess,
              onResponseError = onResponseError,
              onResponseException = onResponseException
            )
          }

          // ---- 응답 바디 파싱 ----
          val transfer = responseHeader[TRANSFER_ENCODING]
          val contentLen =
            responseHeader[CONTENT_LENGTH]?.toLongOrNull()
          val rawBody = when {
            httpStatusCode == NOT_MODIFIED -> byteArrayOf(0)
            transfer?.contains(CHUNKED) == true -> readChunked(bufferedInputStream)
            contentLen != null -> readFixed(bufferedInputStream, contentLen)
            else -> readToEnd(bufferedInputStream)
          }

          // ---- 응답 에러 처리 ----
          if (httpStatusCode in 400..599) {
            onResponseError(
              HttpResponse(
                code = httpStatusCode,
                message = httpStatusMessage,
                body = rawBody.toBytes()
              )
            )
            return@withContext
          }

          // ---- 응답 바디 gzip으로 해제 ----
          val contentType = responseHeader[CONTENT_TYPE]
          val isGzip = responseHeader[CONTENT_ENCODING]?.contains(GZIP) == true
          val bodyBytes =
            if (httpStatusCode != NOT_MODIFIED && isGzip) GZIPInputStream(ByteArrayInputStream(rawBody)).use { it.readBytes() }
            else rawBody

          httpLogger?.printResponseBodyLog(
            body = bodyBytes,
            contentType = contentType,
            wasGzip = isGzip,
            rawSize = if (isGzip) rawBody.size else null
          )

          onResponseSuccess(
            HttpResponse(
              code = httpStatusCode,
              message = httpStatusMessage,
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