package kr.co.architecture.custom.http.client

import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.net.Socket
import java.net.URL
import javax.net.ssl.SNIHostName
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

internal fun readLineAscii(inputStream: InputStream): String? {
  val lineBuffer = ByteArrayOutputStream(64)
  while (true) {
    val byteRead = inputStream.read()
    if (byteRead == -1) {
      return if (lineBuffer.size() == 0) null else lineBuffer.toString(Charsets.US_ASCII.name())
    }
    if (byteRead == '\n'.code) {
      val lineBytes = lineBuffer.toByteArray()
      val lineLength =
        if (lineBytes.isNotEmpty() && lineBytes.last() == '\r'.code.toByte()) lineBytes.size - 1
        else lineBytes.size
      return String(lineBytes, 0, lineLength, Charsets.US_ASCII)
    }
    lineBuffer.write(byteRead)
    if (lineBuffer.size() > 16 * 1024) throw IOException("Header line too long")
  }
}

internal fun readFixed(ins: InputStream, len: Long): ByteArray {
  if (len < 0 || len > Int.MAX_VALUE) throw IOException("Unsupported content-length: $len")
  val buffer = ByteArray(len.toInt())
  var offset = 0
  while (offset < buffer.size) {
    val readCount = ins.read(buffer, offset, buffer.size - offset)
    if (readCount == -1) throw EOFException("Unexpected EOF")
    offset += readCount
  }
  return buffer
}

internal fun readToEnd(ins: InputStream): ByteArray {
  val outBuffer = ByteArrayOutputStream()
  val tempBuffer = ByteArray(8 * 1024)
  while (true) {
    val readCount = ins.read(tempBuffer)
    if (readCount <= 0) break
    outBuffer.write(tempBuffer, 0, readCount)
    if (outBuffer.size() > 32 * 1024 * 1024) throw IOException("Body too large") // 32MB guard
  }
  return outBuffer.toByteArray()
}

internal fun readChunked(ins: InputStream): ByteArray {
  val outBuffer = ByteArrayOutputStream()
  while (true) {
    val chunkSizeLine = readLineAscii(ins) ?: throw IOException("Missing chunk size")
    val semicolonPos = chunkSizeLine.indexOf(';') // ignore chunk extensions
    val hexSizeStr = if (semicolonPos >= 0) chunkSizeLine.substring(0, semicolonPos) else chunkSizeLine
    val chunkSize = hexSizeStr.trim().toInt(16)
    if (chunkSize == 0) {
      // skip trailer headers
      while (true) {
        val trailerLine = readLineAscii(ins) ?: break
        if (trailerLine.isEmpty()) break
      }
      break
    }
    outBuffer.write(readFixed(ins, chunkSize.toLong()))
    val chunkCrlf = readLineAscii(ins)
    if (chunkCrlf == null) throw IOException("Missing CRLF after chunk")
  }
  return outBuffer.toByteArray()
}

internal fun URL.extractPort(): Int = when {
  port in 1..65535 -> port
  // URL.port가 명시돼있지 않은 경우. 기본 -1
  protocol.equals("https", true) -> 443
  protocol.equals("http", true) -> 80
  else -> throw IOException("Unsupported scheme: $protocol")
}

internal fun URL.extractPathAndQuery() = buildString {
  append(if (path.isNullOrEmpty()) "/" else path)
  if (!query.isNullOrEmpty()) append('?').append(query)
}

internal fun getSocket(
  url: URL,
  readTimeoutMs: Int
): Socket {
  val port = url.extractPort()
  return when (url.protocol.equals(HttpHeaderConstants.HTTPS)) {
    true -> {
      // TODO: 여기 왜 이렇게 짬?
      (SSLSocketFactory.getDefault().createSocket(url.host, port) as SSLSocket).apply {
        soTimeout = readTimeoutMs
        // SNI + 호스트네임 검증 활성화
        try {
          sslParameters = sslParameters.apply {
            serverNames = listOf(SNIHostName(url.host))
            endpointIdentificationAlgorithm = HttpHeaderConstants.HTTPS
          }
        } catch (_: Throwable) { /* 일부 구형 기기 호환 */ }
        startHandshake()
      }
    }
    false -> {
      Socket(url.host, port).apply { soTimeout = readTimeoutMs }
    }
  }
}