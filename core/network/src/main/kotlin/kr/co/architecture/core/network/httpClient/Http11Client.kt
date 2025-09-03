package kr.co.architecture.core.network.httpClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.HTTP_1_1
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Method.GET
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.ACCEPT
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.ACCEPT_ENCODING
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONNECTION
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONTENT_ENCODING
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONTENT_LENGTH
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONTENT_TYPE
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.HOST
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.LOCATION
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.TRANSFER_ENCODING
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.USER_AGENT
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.APPLICATION_JSON
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.CHUNKED
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.GZIP
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.KEEP_ALIVE
import kr.co.architecture.core.network.interceptor.CustomHttpLogger
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
  val headers: Map<String, String> = emptyMap(),
  val body: ByteArray = byteArrayOf()
)

class RawHttp11Client(
  private val userAgent: String = "RawHttp11/0.1",
  private val readTimeoutMs: Int = 10_000,
  private val maxRedirects: Int = 5,
  private val httpLogger: CustomHttpLogger? = null
) {

  suspend fun callApi(
    method: String,
    url: String,
    onResponseSuccess: suspend HttpResponse.() -> Unit = {},
    onResponseError: suspend HttpResponse.() -> Unit = {},
    onResponseException: suspend Throwable.() -> Unit = {},
  ) = request(
    method = method,
    url = URL(url),
    body = null,
    redirectDepth = 0,
    onResponseSuccess = onResponseSuccess,
    onResponseError = onResponseError,
    onResponseException = onResponseException,
  )

  private suspend fun request(
    method: String,
    url: URL,
    body: ByteArray?,
    redirectDepth: Int,
    onResponseSuccess: suspend (HttpResponse) -> Unit = {},
    onResponseError: suspend (HttpResponse) -> Unit = {},
    onResponseException: suspend (Throwable) -> Unit = {}
  )  {
    withContext(Dispatchers.IO) {
      try {
// TODO: 필요한가? 만약 설정한다 헀을 때, maxRedirects의 설정 기준은?
        require(redirectDepth <= maxRedirects) { "Too many redirects" }

        val startNs = System.nanoTime()

        val host = url.host
        val pathAndQuery = url.buildPathAndQuery()
        val socket = getSocket(
          url = url,
          readTimeoutMs = readTimeoutMs
        )

        socket.use { s ->
          val bufferedOutputStream = BufferedOutputStream(s.getOutputStream())
          val bufferedInputStream = BufferedInputStream(s.getInputStream())

          // ---- 요청 라인 + 헤더 ----
          // TODO: 왜 LinkedMap을 썼는지?
          val requestHeader = linkedMapOf(
            HOST to host,
            // TODO: userAgent설정 하드코딩해도 괜찮은가?
            USER_AGENT to userAgent,
            ACCEPT to APPLICATION_JSON,
            // TODO: GZIP이 정말 필요한가? 요청/응답 성능 개선이 확실한가?
            ACCEPT_ENCODING to GZIP,
            CONNECTION to KEEP_ALIVE
          ).apply {
            if (body != null) put(CONTENT_LENGTH, "${body.size}")
          }

          httpLogger?.printRequestStartLog(method, "$url", HTTP_1_1)
          httpLogger?.printRequestHeaderLog(requestHeader)
          httpLogger?.printRequestBodyLog()

          val requestHeaderString = buildString {
            append("$method $pathAndQuery $HTTP_1_1\r\n")
            requestHeader.forEach { (k, v) -> append("$k: $v\r\n") }
            append("\r\n")
          }
          bufferedOutputStream.run {
            write(requestHeaderString.toByteArray(Charsets.US_ASCII))
            if (body != null) write(body)
            flush()
          }

          // ---- 상태줄 ----
          val statusLine = readLineAscii(bufferedInputStream) ?: throw IOException("No status line")
          val parts = statusLine.split(' ', limit = 3)
          val code = parts.getOrNull(1)?.toIntOrNull() ?: throw IOException("Bad status: $statusLine")
          val message = parts.getOrNull(2) ?: ""

          // ---- 헤더 ----
          val responseHeader = mutableMapOf<String, String>()
          while (true) {
            val line = readLineAscii(bufferedInputStream) ?: break
            if (line.isEmpty()) break
            val splitIndex = line.indexOf(':')
            if (splitIndex > 0) {
              val property = line.substring(0, splitIndex).trim().lowercase(Locale.US)
              val value = line.substring(splitIndex + 1).trim()
              responseHeader[property] = value
            }
          }

          val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
          httpLogger?.printResponseStartLog(code, message, "$url", tookMs)
          httpLogger?.printResponseHeaderLog(responseHeader)

          // ---- 리다이렉트 처리 ----
          if (code in listOf(301, 302, 303, 307, 308)) {
            // TODO: 무슨뜻이지?
            val location = responseHeader[LOCATION] ?: throw IOException("Redirect without Location")
            val redirectUrl = URL(url, location) // 상대/절대 모두 처리
            return@withContext request(
              method = if (code == 303) GET else method,
              url = redirectUrl,
              body = if (code >= 307) body else null,
              redirectDepth = redirectDepth + 1
            )
          }

          // ---- 바디 경계 판별 ----
          val transfer = responseHeader[TRANSFER_ENCODING]
          val contentLen = responseHeader[CONTENT_LENGTH]?.toLongOrNull()
          val rawBody = when {
            transfer?.contains(CHUNKED) == true -> readChunked(bufferedInputStream)
            contentLen != null -> readFixed(bufferedInputStream, contentLen)
            else -> readToEnd(bufferedInputStream)
          }

          if (code in 400..599) {
            onResponseError(
              HttpResponse(
                code = code,
                message = message,
                body = rawBody
              )
            )
            return@withContext
          }

          // ---- gzip 해제 ----
          val isGzip = responseHeader[CONTENT_ENCODING]?.contains(GZIP) == true
          val bodyBytes =
            if (isGzip) GZIPInputStream(ByteArrayInputStream(rawBody)).use { it.readBytes() }
            else rawBody

          val contentType = responseHeader[CONTENT_TYPE]
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
              headers = responseHeader,
              body = bodyBytes
            )
          )
        }
      } catch (e: Exception) { onResponseException(e) }
    }
  }
}