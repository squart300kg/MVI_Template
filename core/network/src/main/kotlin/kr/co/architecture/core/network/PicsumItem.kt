package kr.co.architecture.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset
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
        request(method = "GET", url = URL(url), headers = headers, body = null, redirectDepth = 0)

    private suspend fun request(
        method: String,
        url: URL,
        headers: Map<String, String>,
        body: ByteArray?,
        redirectDepth: Int
    ): HttpResponse = withContext(Dispatchers.IO) {
        require(redirectDepth <= maxRedirects) { "Too many redirects" }

        val host = url.host
        val port = if (url.port != -1) url.port else if (url.protocol.equals("https", true)) 443 else 80
        val pathAndQuery = buildString {
            append(if (url.path.isNullOrEmpty()) "/" else url.path)
            if (!url.query.isNullOrEmpty()) append('?').append(url.query)
        }

        val socket = if (url.protocol.equals("https", true)) {
            (SSLSocketFactory.getDefault().createSocket(host, port) as SSLSocket).apply {
                soTimeout = readTimeoutMs
                // SNI + 호스트네임 검증 활성화
                try {
                    val p = sslParameters
                    p.serverNames = listOf(SNIHostName(host))
                    p.endpointIdentificationAlgorithm = "HTTPS"
                    sslParameters = p
                } catch (_: Throwable) { /* 일부 구형 기기 호환 */ }
                startHandshake()
            }
        } else {
            java.net.Socket(host, port).apply { soTimeout = readTimeoutMs }
        }

        socket.use { s ->
            val out = BufferedOutputStream(s.getOutputStream())
            val ins = BufferedInputStream(s.getInputStream())

            // ---- 요청 라인 + 헤더 ----
            val sb = StringBuilder().apply {
                append("$method $pathAndQuery HTTP/1.1\r\n")
                append("Host: $host\r\n")
                append("User-Agent: $userAgent\r\n")
                append("Accept: application/json\r\n")
                append("Accept-Encoding: gzip\r\n") // 서버가 gzip으로 줄 수 있음(네가 올린 로그와 일치)
                append("Connection: close\r\n")      // 구현 단순화 (필요 시 keep-alive로 변경 가능)
                headers.forEach { (k, v) -> append("$k: $v\r\n") }
                if (body != null) append("Content-Length: ${body.size}\r\n")
                append("\r\n")
            }
            out.write(sb.toString().toByteArray(Charsets.US_ASCII))
            if (body != null) out.write(body)
            out.flush()

            // ---- 상태줄 ----
            val statusLine = readLineAscii(ins) ?: throw IOException("No status line")
            val parts = statusLine.split(' ', limit = 3)
            val code = parts.getOrNull(1)?.toIntOrNull() ?: throw IOException("Bad status: $statusLine")
            val message = parts.getOrNull(2) ?: ""

            // ---- 헤더 ----
            val headerMap = mutableMapOf<String, String>()
            while (true) {
                val line = readLineAscii(ins) ?: break
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
                return@withContext request(nextMethod, next, headers, if (code >= 307) body else null, redirectDepth + 1)
            }

            // ---- 바디 경계 판별 ----
            val transfer = headerMap["transfer-encoding"]?.lowercase(Locale.US)
            val contentLen = headerMap["content-length"]?.toLongOrNull()
            val rawBody = when {
                transfer?.contains("chunked") == true -> readChunked(ins)
                contentLen != null -> readFixed(ins, contentLen)
                else -> readToEnd(ins)
            }

            // ---- gzip 해제 ----
            val bodyBytes =
                if (headerMap["content-encoding"]?.lowercase(Locale.US)?.contains("gzip") == true) {
                    GZIPInputStream(ByteArrayInputStream(rawBody)).use { it.readBytes() }
                } else rawBody

            HttpResponse(code, message, headerMap, bodyBytes)
        }
    }

    // ----- 유틸: ASCII 라인 읽기 (CRLF 처리) -----
    private fun readLineAscii(ins: InputStream): String? {
        val baos = ByteArrayOutputStream(64)
        while (true) {
            val b = ins.read()
            if (b == -1) return if (baos.size() == 0) null else baos.toString(Charsets.US_ASCII.name())
            if (b == '\n'.code) {
                val arr = baos.toByteArray()
                val len = if (arr.isNotEmpty() && arr.last() == '\r'.code.toByte()) arr.size - 1 else arr.size
                return String(arr, 0, len, Charsets.US_ASCII)
            }
            baos.write(b)
            if (baos.size() > 16 * 1024) throw IOException("Header line too long")
        }
    }

    private fun readFixed(ins: InputStream, len: Long): ByteArray {
        if (len < 0 || len > Int.MAX_VALUE) throw IOException("Unsupported content-length: $len")
        val buf = ByteArray(len.toInt())
        var off = 0
        while (off < buf.size) {
            val r = ins.read(buf, off, buf.size - off)
            if (r == -1) throw EOFException("Unexpected EOF")
            off += r
        }
        return buf
    }

    private fun readToEnd(ins: InputStream): ByteArray {
        val baos = ByteArrayOutputStream()
        val buf = ByteArray(8 * 1024)
        while (true) {
            val r = ins.read(buf)
            if (r <= 0) break
            baos.write(buf, 0, r)
            if (baos.size() > 32 * 1024 * 1024) throw IOException("Body too large") // 32MB 가드
        }
        return baos.toByteArray()
    }

    private fun readChunked(ins: InputStream): ByteArray {
        val baos = ByteArrayOutputStream()
        while (true) {
            val sizeLine = readLineAscii(ins) ?: throw IOException("Missing chunk size")
            val semi = sizeLine.indexOf(';') // 확장 파라미터 무시
            val hex = if (semi >= 0) sizeLine.substring(0, semi) else sizeLine
            val size = hex.trim().toInt(16)
            if (size == 0) {
                // 트레일러 헤더 스킵
                while (true) {
                    val trailer = readLineAscii(ins) ?: break
                    if (trailer.isEmpty()) break
                }
                break
            }
            baos.write(readFixed(ins, size.toLong()))
            // 청크 뒤의 CRLF 소비
            val crlf = readLineAscii(ins) // 빈 줄이어야 함
            if (crlf == null) throw IOException("Missing CRLF after chunk")
        }
        return baos.toByteArray()
    }
}

class PicsumApi(private val http: RawHttp11Client = RawHttp11Client()) {

    suspend fun getList(page: Int, limit: Int): PageResult {
        val url = "https://picsum.photos/v2/list?page=$page&limit=$limit"
        val resp = http.get(
            url = url,
            headers = emptyMap() // 필요 시 커스텀 헤더 추가
        )
        if (resp.code != 200) throw IOException("HTTP ${resp.code} ${resp.message}")

        // 헤더: content-type 검증(선택)
        val ct = resp.headers["content-type"]?.lowercase(Locale.US)
        if (ct != null && !ct.startsWith("application/json")) {
            throw IOException("Unexpected content-type: $ct")
        }

        // 캐시 제어: no-store → 목록은 캐시하지 않음 (네가 준 헤더를 존중)
        // val cacheControl = resp.headers["cache-control"]

        // Link 파싱 (prev/next)
        val linkMap = parseLinkHeader(resp.headers["link"])
        val prev = linkMap["prev"]
        val next = linkMap["next"]

        // JSON 파싱 (org.json 사용; 외부 JSON 라이브러리 사용 금지 제약 준수)
        val arr = JSONArray(resp.body.toString(Charset.forName("UTF-8")))
        val items = buildList(arr.length()) {
            for (i in 0 until arr.length()) {
                val o: JSONObject = arr.getJSONObject(i)
                add(
                    PicsumItem(
                        id = o.getString("id"),
                        author = o.getString("author"),
                        width = o.getInt("width"),
                        height = o.getInt("height"),
                        url = o.getString("url"),
                        downloadUrl = o.getString("download_url")
                    )
                )
            }
        }

        return PageResult(items = items, prev = prev, next = next)
    }
}