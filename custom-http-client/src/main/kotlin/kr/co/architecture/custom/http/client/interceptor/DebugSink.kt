package kr.co.architecture.custom.http.client.interceptor

import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class CustomHttpLogger(
  private val maxBodyChars: Int = 16_384
) {
  fun printRequestStartLog(method: String, url: String, httpVersion: String) {
    println("--> $method $url $httpVersion")
  }

  fun printRequestHeaderLog(headers: Map<String, String>) {
    headers.forEach { (k, v) -> println("$k: $v") }
  }

  fun printRequestBodyLog() {
    println("")
    println("--> END (no body)")
  }

  fun printResponseStartLog(code: Int, message: String, url: String, tookMs: Long) {
    println("<-- $code $message $url (${tookMs}ms)")
  }

  fun printResponseHeaderLog(headers: Map<String, String>) {
    headers.forEach { (k, v) -> println("$k: $v") }
  }

  fun printResponseBodyLog(
    body: ByteArray,
    contentType: String?,
    wasGzip: Boolean,
    rawSize: Int? = null
  ) {
    val info = buildString {
      append("${body.size}-byte body")
      if (wasGzip && rawSize != null) append(" (gzip decompressed from $rawSize bytes)")
    }

    val isImage = contentType
      ?.lowercase(Locale.US)
      ?.startsWith("image/") == true
    if (isImage) {
      println("<-- END HTTP (binary $info omitted)");
      return
    }

    val text = body.toString(Charsets.UTF_8)
    val pretty = prettyJsonOrNull(text) ?: text
    println("")
    println(clip(pretty))
    println("<-- END HTTP ($info)")
  }

  private fun prettyJsonOrNull(s: String): String? = try {
    val t = s.trimStart()
    when {
      t.startsWith("{") -> JSONObject(s).toString(2)
      t.startsWith("[") -> JSONArray(s).toString(2)
      else -> null
    }
  } catch (_: Throwable) { null }

  private fun clip(text: String): String =
    if (text.length > maxBodyChars) text.substring(0, maxBodyChars) + "…(clipped)" else text
}