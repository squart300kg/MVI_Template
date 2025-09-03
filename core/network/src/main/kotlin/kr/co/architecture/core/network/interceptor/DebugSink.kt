package kr.co.architecture.core.network.interceptor

import java.nio.charset.Charset
import java.util.Locale

/**
 * BODY 레벨 전용 HTTP 로거.
 * - enabled=false면 완전 무음
 * - 텍스트/JSON 등만 본문 출력, 바이너리는 길이 정보만 출력
 */
class CustomHttpLogger(
    private val enabled: Boolean = true,
    private val maxBodyChars: Int = 16_384,
    private val defaultCharset: Charset = Charsets.UTF_8
) {

    private fun on() = enabled

    // ------- Request -------
    fun onRequestStart(method: String, url: String, httpVersion: String) {
        if (!on()) return
        println("--> $method $url $httpVersion")
    }

    fun onRequestHeaders(headers: Map<String, String>) {
        if (!on()) return
        headers.forEach { (k, v) -> println("$k: $v") }
    }

    fun onRequestBody(body: ByteArray?, contentType: String? = null) {
        if (!on()) return
        if (body == null || body.isEmpty()) {
            println("--> END (no body)")
            return
        }
        if (isProbablyText(contentType)) {
            val text = runCatching { String(body, detectCharset(contentType)) }
                .getOrElse { String(body, defaultCharset) }
            println("")
            println(clip(text))
            println("--> END ${body.size}-byte body (text)")
        } else {
            println("--> END ${body.size}-byte body (binary omitted)")
        }
    }

    // ------- Response -------
    fun onResponseStart(code: Int, message: String, url: String, tookMs: Long) {
        if (!on()) return
        println("<-- $code $message $url (${tookMs}ms)")
    }

    fun onResponseHeaders(headers: Map<String, String>) {
        if (!on()) return
        headers.forEach { (k, v) -> println("$k: $v") }
    }

    fun onResponseBody(
        body: ByteArray,
        contentType: String?,
        wasGzip: Boolean,
        rawSize: Int? = null
    ) {
        if (!on()) return
        val info = buildString {
            append("${body.size}-byte body")
            if (wasGzip && rawSize != null) append(" (gzip decompressed from $rawSize bytes)")
        }

        if (isProbablyText(contentType)) {
            val text = runCatching { String(body, detectCharset(contentType)) }
                .getOrElse { String(body, defaultCharset) }
            println("")
            println(clip(text))
            println("<-- END HTTP ($info)")
        } else {
            println("<-- END HTTP (binary $info omitted)")
        }
    }

    // ------- helpers -------
    private fun isProbablyText(contentType: String?): Boolean {
        if (contentType == null) return false
        val ct = contentType.lowercase(Locale.US)
        return ct.startsWith("text/") ||
               ct.contains("json") || ct.contains("xml") ||
               ct.contains("javascript") || ct.contains("html")
    }

    private fun detectCharset(contentType: String?): Charset {
        if (contentType == null) return defaultCharset
        val lower = contentType.lowercase(Locale.US)
        val idx = lower.indexOf("charset=")
        if (idx >= 0) {
            val v = lower.substring(idx + 8).trim().trim('"', '\'')
            return runCatching { Charset.forName(v) }.getOrDefault(defaultCharset)
        }
        return defaultCharset
    }

    private fun clip(text: String): String =
        if (text.length > maxBodyChars) text.substring(0, maxBodyChars) + "…(clipped)" else text
}