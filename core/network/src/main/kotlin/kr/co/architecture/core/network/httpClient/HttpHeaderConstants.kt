package kr.co.architecture.core.network.httpClient

object HttpHeaderConstants {
  const val HTTPS = "https"
  const val HTTP = "http"
  const val HTTP_1_1 = "HTTP/1.1"

  object Property {
    const val HOST = "Host"
    const val USER_AGENT = "User-Agent"
    const val ACCEPT = "Accept"
    const val ACCEPT_ENCODING = "Accept-Encoding"
    const val CONNECTION = "Connection"
    const val CONTENT_LENGTH = "Content-Length"
    const val CONTENT_ENCODING = "Content-Encoding"
    const val TRANSFER_ENCODING = "Transfer-Encoding"
    const val LOCATION = "LOCATION"
  }
  object Value {
    const val APPLICATION_JSON = "application/json"
    const val GZIP = "gzip"
    const val CLOSE = "close"
    const val KEEP_ALIVE = "keep-alive"
    const val CHUNKED = "chunked"
  }
}