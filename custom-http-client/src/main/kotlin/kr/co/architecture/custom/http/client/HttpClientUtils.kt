package kr.co.architecture.custom.http.client

import kr.co.architecture.custom.http.client.HttpBufferConfig.BODY_MAX_ACCUMULATED_BYTES
import kr.co.architecture.custom.http.client.HttpBufferConfig.HEADER_LINE_INITIAL_CAPACITY_BYTES
import kr.co.architecture.custom.http.client.HttpBufferConfig.HEADER_LINE_MAX_BYTES
import kr.co.architecture.custom.http.client.HttpBufferConfig.STREAM_COPY_BUFFER_BYTES
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.HTTP
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.HTTPS
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.net.Socket
import java.net.URL
import javax.net.ssl.SNIHostName
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

private object HttpBufferConfig {
  /** 헤더 한 줄 읽기 시작 시 BAOS 초기 용량 */
  const val HEADER_LINE_INITIAL_CAPACITY_BYTES: Int = 64 // 64B
  /** 헤더 한 줄 최대 허용 바이트(가드) */
  const val HEADER_LINE_MAX_BYTES: Int = 16 * 1024 // 16KB

  /** 스트림 copy용 임시 버퍼 크기 */
  const val STREAM_COPY_BUFFER_BYTES: Int = 8 * 1024 // 8KB
  /** 본문 누적 최대 허용 바이트(가드) */
  const val BODY_MAX_ACCUMULATED_BYTES: Int = 32 * 1024 * 1024 // 32MB
}

internal fun readLineAscii(inputStream: InputStream): String? {
  val lineBuffer = ByteArrayOutputStream(HEADER_LINE_INITIAL_CAPACITY_BYTES)
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
    if (lineBuffer.size() > HEADER_LINE_MAX_BYTES) throw IOException("Header line too long")
  }
}

internal fun readFixed(inputStream: InputStream, contentLength: Long): ByteArray {
  if (contentLength < 0 || contentLength > Int.MAX_VALUE) throw IOException("Unsupported content-length: $contentLength")
  val buffer = ByteArray(contentLength.toInt())
  var offset = 0
  while (offset < buffer.size) {
    val readCount = inputStream.read(buffer, offset, buffer.size - offset)
    if (readCount == -1) throw EOFException("Unexpected EOF")
    offset += readCount
  }
  return buffer
}

internal fun readToEnd(inputStream: InputStream): ByteArray {
  val outBuffer = ByteArrayOutputStream()
  val tempBuffer = ByteArray(STREAM_COPY_BUFFER_BYTES)
  while (true) {
    val readCount = inputStream.read(tempBuffer)
    if (readCount <= 0) break
    outBuffer.write(tempBuffer, 0, readCount)
    if (outBuffer.size() > BODY_MAX_ACCUMULATED_BYTES) throw IOException("Body too large") // 32MB guard
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
  protocol.equals(HTTPS, true) -> 443
  protocol.equals(HTTP, true) -> 80
  else -> throw IOException("Unsupported scheme: $protocol")
}

internal fun URL.extractPathAndQuery() = buildString {
  append(if (path.isNullOrEmpty()) "/" else path)
  if (!query.isNullOrEmpty()) append('?').append(query)
}

// TODO: 싱글톤 여부 고려
internal fun getSocket(
  url: URL,
  readTimeoutMs: Int
): Socket {
  val port = url.extractPort()
  return when (url.protocol.equals(HTTPS)) {
    true -> {
      (SSLSocketFactory.getDefault().createSocket(url.host, port) as SSLSocket).apply {
        soTimeout = readTimeoutMs
        sslParameters.apply {
          serverNames = listOf(SNIHostName(url.host))
          endpointIdentificationAlgorithm = HTTPS
        }
        startHandshake()
      }
    }
    false -> {
      Socket(url.host, port).apply { soTimeout = readTimeoutMs }
    }
  }
}