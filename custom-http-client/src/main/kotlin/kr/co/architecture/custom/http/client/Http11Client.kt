package kr.co.architecture.custom.http.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.HTTP_1_1
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.ACCEPT
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.ACCEPT_ENCODING
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.CONNECTION
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.CONTENT_ENCODING
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.CONTENT_LENGTH
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.CONTENT_TYPE
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.HOST
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.LOCATION
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.TRANSFER_ENCODING
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Property.USER_AGENT
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Value.CHUNKED
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Value.GZIP
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Value.IMAGE_ALL
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Value.KEEP_ALIVE
import kr.co.architecture.custom.http.client.constants.HttpStatusCode.MOVED_TEMP
import kr.co.architecture.custom.http.client.constants.HttpStatusCode.NOT_MODIFIED
import kr.co.architecture.custom.http.client.model.Address
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
  private val maxRetryWhenConnectTimeout: Int,
  private val connectTimeoutMs: Int,
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
      maxRetryWhenConnectTimeout: Int = 3,
      connectTimeoutMs: Int = 10_000,
      readTimeoutMs: Int = 10_000,
      maxRedirects: Int = 3,
      httpLogger: CustomHttpLogger? = null
    ): RawHttp11Client {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: RawHttp11Client(
          userAgent = userAgent,
          maxRetryWhenConnectTimeout = maxRetryWhenConnectTimeout,
          connectTimeoutMs = connectTimeoutMs,
          readTimeoutMs = readTimeoutMs,
          maxRedirects = maxRedirects,
          httpLogger = httpLogger,
        ).also { INSTANCE = it }
      }
    }
  }

  private val connectionPool by lazy(
    mode = LazyThreadSafetyMode.PUBLICATION
  ) { ConnectionPool.getInstance() }

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

  /**
   * 총 11개의 step으로 진행
   */
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

        // 1) hand-shake를 위한 주소 설정
        val host = url.host
        val pathAndQuery = url.extractPathAndQuery()
        val address = Address(
          scheme = url.protocol.lowercase(),
          host = url.host,
          port = url.extractPort()
        )

        // 2) 소켓 획득 (있으면 재사용, 없으면 새로)
        val socket = connectionPool.acquire(
          address = address,
          maxRetryWhenConnectTimeout = maxRetryWhenConnectTimeout,
          connectTimeoutMs = connectTimeoutMs,
          readTimeoutMs = readTimeoutMs
        )

        try {
          val bufferedOutputStream = BufferedOutputStream(socket.getOutputStream())
          val bufferedInputStream = BufferedInputStream(socket.getInputStream())

          // 3) 요청 헤더 구축
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

            // 4) API 호출
            flush()
          }

          // ===== API 요청 로그 ====
          httpLogger?.printRequestStartLog(method, "$url", HTTP_1_1)
          httpLogger?.printRequestHeaderLog(requestHeader)
          httpLogger?.printRequestBodyLog()

          // 5) 응답 헤더 상태줄 파싱 (HTTP/1.1 200 OK)
          val (httpStatusCode, httpStatusMessage) = run {
            readLineAscii(bufferedInputStream)?.let { statusLine ->
              statusLine.split(' ', limit = 3).run {
                val code = getOrNull(1)?.toIntOrNull() ?: throw IOException("http status code is abnormal: $statusLine")
                val message = getOrNull(2) ?: throw IOException("http status message is abnormal: $statusLine")
                code to message
              }
            } ?: throw IOException("http status line is null")
          }

          // 6) 응답 헤더 본문 파싱
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
          val isKeepAlive = responseHeader[CONNECTION]?.lowercase()  == KEEP_ALIVE
          val transferEncoding = responseHeader[TRANSFER_ENCODING]
          val contentLength = responseHeader[CONTENT_LENGTH]?.toLongOrNull()
          val contentType = responseHeader[CONTENT_TYPE]
          val isGzip = responseHeader[CONTENT_ENCODING]?.contains(GZIP) == true

          // ===== API 응답 로그 ====
          val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
          httpLogger?.printResponseStartLog(httpStatusCode, httpStatusMessage, "$url", tookMs)
          httpLogger?.printResponseHeaderLog(responseHeader)

          // 7) 리다이렉트 진행
          if (httpStatusCode == MOVED_TEMP) {
            val location = responseHeader[LOCATION] ?: throw IOException("Redirect without Location")

            // 7-1) 리다이렉트 직전, socket release or close
            if (isKeepAlive) connectionPool.release(address, socket)
            else runCatching { socket.close() }

            // 7-2) 리다이렉트 요청
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

          // 8) 응답 바디 파싱
          val rawBody = when {
            httpStatusCode == NOT_MODIFIED -> byteArrayOf(0)
            transferEncoding?.contains(CHUNKED) == true -> readChunked(bufferedInputStream)
            contentLength != null -> readFixed(bufferedInputStream, contentLength)
            else -> readToEnd(bufferedInputStream)
          }

          // 9) 응답 바디 gzip으로 해제
          val bodyBytes =
            if (httpStatusCode != NOT_MODIFIED && isGzip) GZIPInputStream(ByteArrayInputStream(rawBody)).use { it.readBytes() }
            else rawBody

          // ===== API 응답 로그 ====
          httpLogger?.printResponseBodyLog(
            body = bodyBytes,
            contentType = contentType,
            wasGzip = isGzip,
            rawSize = if (isGzip) rawBody.size else null
          )

          // 10) 응답 직전, socket release or close
          if (isKeepAlive) connectionPool.release(address, socket)
          else runCatching { socket.close() }

          val response = HttpResponse(
            code = httpStatusCode,
            message = httpStatusMessage,
            header = responseHeader,
            body = bodyBytes.toBytes()
          )

          // 11) 응답 결과 반환
          if (httpStatusCode in 400..599) onResponseError(response)
          else onResponseSuccess(response)
        } catch (e: Exception) {
          runCatching { socket.close() }
          onResponseException(e)
        }
      } catch (e: Exception) { onResponseException(e) }
    }
  }
}