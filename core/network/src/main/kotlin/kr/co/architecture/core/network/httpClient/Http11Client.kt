package kr.co.architecture.core.network.httpClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.HTTP_1_1
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Method.GET
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.ACCEPT
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.ACCEPT_ENCODING
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONNECTION
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONTENT_LENGTH
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.HOST
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
  val headers: Map<String, String>, // 소문자 키
  val body: ByteArray
)

class RawHttp11Client(
  private val userAgent: String = "RawHttp11/0.1",
  private val readTimeoutMs: Int = 10_000,
  private val maxRedirects: Int = 5,
  private val httpLogger: CustomHttpLogger? = null
) {

  suspend fun get(url: String, headers: Map<String, String> = emptyMap()): HttpResponse =
    request(method = GET, url = URL(url), header = headers, body = null, redirectDepth = 0)

  private suspend fun request(
    method: String,
    url: URL,
    header: Map<String, String>,
    body: ByteArray?,
    redirectDepth: Int
  ): HttpResponse = withContext(Dispatchers.IO) {
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
      val reqHeaders = linkedMapOf(
        HOST to host,
        // TODO: userAgent설정 이대로 괜찮은가?
        USER_AGENT to userAgent,
        ACCEPT to APPLICATION_JSON,
        // TODO: GZIP이 정말 필요한가?
        ACCEPT_ENCODING to GZIP,
        CONNECTION to KEEP_ALIVE
      ).apply {
        if (body != null) put(CONTENT_LENGTH, body.size.toString())
        header.forEach { (k, v) -> put(k, v) }
      }

      // ---- REQUEST LOG (BODY 레벨 고정) ----
      httpLogger?.onRequestStart(method, url.toString(), HTTP_1_1)
      httpLogger?.onRequestHeaders(reqHeaders)
      httpLogger?.onRequestBody(body)

      val head = buildString {
        append("$method $pathAndQuery $HTTP_1_1\r\n")
        reqHeaders.forEach { (k,v) -> append("$k: $v\r\n") }
        append("\r\n")
      }
      bufferedOutputStream.run {
        write(head.toByteArray(Charsets.US_ASCII))
        if (body != null) write(body)
        flush()
      }

      // ---- 상태줄 ----
      val statusLine = readLineAscii(bufferedInputStream) ?: throw IOException("No status line")
      val parts = statusLine.split(' ', limit = 3)
      val code = parts.getOrNull(1)?.toIntOrNull() ?: throw IOException("Bad status: $statusLine")
      val message = parts.getOrNull(2) ?: ""

      // ---- 헤더 ----
      val headerMap = mutableMapOf<String, String>()
      while (true) {
        val line = readLineAscii(bufferedInputStream) ?: break
        if (line.isEmpty()) break // 빈 줄 == 헤더 끝
        val idx = line.indexOf(':')
        if (idx > 0) {
          val k = line.substring(0, idx).trim().lowercase(Locale.US)
          val v = line.substring(idx + 1).trim()
          // 같은 키가 중복되면 마지막 값으로 (필요 시 append)
          headerMap[k] = v
        }
      }

      val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

      // ---- RESPONSE LOG (status+headers) ----
      httpLogger?.onResponseStart(code, message, url.toString(), tookMs)
      httpLogger?.onResponseHeaders(headerMap)

      // ---- 리다이렉트 처리 ----
      if (code in listOf(301, 302, 303, 307, 308)) {
        val location = headerMap["location"] ?: throw IOException("Redirect without Location")
        val next = URL(url, location) // 상대/절대 모두 처리
        val nextMethod = if (code == 303) GET else method
        return@withContext request(
          nextMethod,
          next,
          header,
          if (code >= 307) body else null,
          redirectDepth + 1
        )
      }

      // ---- 바디 경계 판별 ----
      val transfer = headerMap["transfer-encoding"]?.lowercase(Locale.US)
      val contentLen = headerMap["content-length"]?.toLongOrNull()
      val rawBody = when {
        transfer?.contains(CHUNKED) == true -> readChunked(bufferedInputStream)
        contentLen != null -> readFixed(bufferedInputStream, contentLen)
        else -> readToEnd(bufferedInputStream)
      }

      // ---- gzip 해제 ----
      val isGzip = headerMap["content-encoding"]?.lowercase(Locale.US)?.contains(GZIP) == true
      val bodyBytes =
        if (isGzip) GZIPInputStream(ByteArrayInputStream(rawBody)).use { it.readBytes() }
        else rawBody

      val contentType = headerMap["content-type"]
      httpLogger?.onResponseBody(
        body = bodyBytes,
        contentType = contentType,
        wasGzip = isGzip,
        rawSize = if (isGzip) rawBody.size else null
      )

      HttpResponse(code, message, headerMap, bodyBytes)
    }
  }
}