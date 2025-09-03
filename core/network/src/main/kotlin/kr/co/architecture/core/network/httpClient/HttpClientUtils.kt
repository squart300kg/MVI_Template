package kr.co.architecture.core.network.httpClient

import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.HTTPS
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.HTTP_1_1
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.net.Socket
import java.net.URL
import javax.net.ssl.SNIHostName
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import kotlin.collections.component1
import kotlin.collections.component2

internal fun readLineAscii(ins: InputStream): String? {
  val baos = ByteArrayOutputStream(64)
  while (true) {
    val b = ins.read()
    if (b == -1) return if (baos.size() == 0) null else baos.toString(Charsets.US_ASCII.name())
    if (b == '\n'.code) {
      val arr = baos.toByteArray()
      val len =
        if (arr.isNotEmpty() && arr.last() == '\r'.code.toByte()) arr.size - 1 else arr.size
      return String(arr, 0, len, Charsets.US_ASCII)
    }
    baos.write(b)
    if (baos.size() > 16 * 1024) throw IOException("Header line too long")
  }
}

internal fun readFixed(ins: InputStream, len: Long): ByteArray {
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

internal fun readToEnd(ins: InputStream): ByteArray {
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

internal fun readChunked(ins: InputStream): ByteArray {
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

internal fun URL.extractPort(): Int = when {
  port in 1..65535 -> port
  // URL.port가 명시돼있지 않은 경우. 기본 -1
  protocol.equals("https", true) -> 443
  protocol.equals("http", true) -> 80
  else -> throw IOException("Unsupported scheme: $protocol")
}

internal fun URL.buildPathAndQuery() = buildString {
  append(if (path.isNullOrEmpty()) "/" else path)
  if (!query.isNullOrEmpty()) append('?').append(query)
}

internal fun getSocket(
  url: URL,
  readTimeoutMs: Int
): Socket {
  val port = url.extractPort()
  return when (url.protocol.equals(HTTPS)) {
    true -> {
      (SSLSocketFactory.getDefault().createSocket(url.host, port) as SSLSocket).apply {
        soTimeout = readTimeoutMs
        // SNI + 호스트네임 검증 활성화
        try {
          sslParameters = sslParameters.apply {
            serverNames = listOf(SNIHostName(url.host))
            endpointIdentificationAlgorithm = HTTPS
          }
        } catch (_: Throwable) { /* 일부 구형 기기 호환 */
        }
        startHandshake()
      }
    }
    false -> {
      Socket(url.host, port).apply { soTimeout = readTimeoutMs }
    }
  }
}