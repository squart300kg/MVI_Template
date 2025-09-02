package kr.co.architecture.core.network.httpClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.HTTPS
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.HTTP_1_1
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.ACCEPT
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.ACCEPT_ENCODING
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONNECTION
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.CONTENT_LENGTH
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.HOST
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Property.USER_AGENT
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.APPLICATION_JSON
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.GZIP
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.KEEP_ALIVE
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.Socket
import java.net.URL
import java.util.Locale
import java.util.zip.GZIPInputStream
import javax.net.ssl.SNIHostName
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

data class PicsumItem(
  val id: String,
  val author: String,
  val width: Int,
  val height: Int,
  val url: String,
  val downloadUrl: String
)

data class PageResult(
  val items: List<PicsumItem>,
  val prev: String?,
  val next: String?
)

data class HttpResponse(
  val code: Int,
  val message: String,
  val headers: Map<String, String>, // 소문자 키
  val body: ByteArray
)

private val LINK_REGEX = Regex("""<([^>]+)>\s*;\s*rel="([^"]+)"""")
fun parseLinkHeader(header: String?): Map<String, String> {
  if (header.isNullOrBlank()) return emptyMap()
  return LINK_REGEX.findAll(header).associate { it.groupValues[2] to it.groupValues[1] }
}

class RawHttp11Client(
  private val userAgent: String = "RawHttp11/0.1",
  private val readTimeoutMs: Int = 10_000,
  private val maxRedirects: Int = 5
) {

  suspend fun get(url: String, headers: Map<String, String> = emptyMap()): HttpResponse =
    request(method = "GET", url = URL(url), header = headers, body = null, redirectDepth = 0)

  private suspend fun request(
    method: String,
    url: URL,
    header: Map<String, String>,
    body: ByteArray?,
    redirectDepth: Int
  ): HttpResponse = withContext(Dispatchers.IO) {
    require(redirectDepth <= maxRedirects) { "Too many redirects" }

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
      val sb = StringBuilder().apply {
        append("$method $pathAndQuery $HTTP_1_1\r\n")
        append("${HOST}: $host\r\n")
        append("$USER_AGENT: $userAgent\r\n")
        append("$ACCEPT: $APPLICATION_JSON\r\n")
        append("$ACCEPT_ENCODING: $GZIP\r\n") // 서버가 gzip으로 줄 수 있음(네가 올린 로그와 일치)
        append("$CONNECTION: $KEEP_ALIVE\r\n") // 구현 단순화 (필요 시 keep-alive로 변경 가능)
        header.forEach { (key, value) -> append("$key: $value\r\n") }
        if (body != null) append("$CONTENT_LENGTH: ${body.size}\r\n")
        append("\r\n")
      }
      bufferedOutputStream.run {
        write(sb.toString().toByteArray(Charsets.US_ASCII))
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

      // ---- 리다이렉트 처리 ----
      if (code in listOf(301, 302, 303, 307, 308)) {
        val location = headerMap["location"] ?: throw IOException("Redirect without Location")
        val next = URL(url, location) // 상대/절대 모두 처리
        val nextMethod = if (code == 303) "GET" else method
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
        transfer?.contains("chunked") == true -> readChunked(bufferedInputStream)
        contentLen != null -> readFixed(bufferedInputStream, contentLen)
        else -> readToEnd(bufferedInputStream)
      }

      // ---- gzip 해제 ----
      val bodyBytes =
        if (headerMap["content-encoding"]?.lowercase(Locale.US)?.contains("gzip") == true) {
          GZIPInputStream(ByteArrayInputStream(rawBody)).use { it.readBytes() }
        } else rawBody

      HttpResponse(code, message, headerMap, bodyBytes)
    }
  }
}