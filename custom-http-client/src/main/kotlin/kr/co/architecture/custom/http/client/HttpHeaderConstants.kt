package kr.co.architecture.custom.http.client

object HttpHeaderConstants {
  object Method {
    const val GET = "GET"
    const val HEAD = "HEAD"
    const val POST = "POST"
    const val PUT = "PUT"
    const val DELETE = "DELETE"
  }

  const val HTTPS = "https"
  const val HTTP = "http"
  const val HTTP_1_1 = "HTTP/1.1"

  object Property {
    const val HOST = "host"
    const val USER_AGENT = "user-agent"
    const val ACCEPT = "accept"
    const val ACCEPT_ENCODING = "accept-encoding"
    const val CONNECTION = "connection"
    const val CONTENT_TYPE = "content-type"
    const val CONTENT_LENGTH = "content-length"
    const val CONTENT_ENCODING = "content-encoding"
    const val TRANSFER_ENCODING = "transfer-encoding"
    const val LOCATION = "location"
    const val LINK = "link"
  }
  object Value {
    const val APPLICATION_JSON = "application/json"
    const val GZIP = "gzip"
    const val CLOSE = "close"
    const val KEEP_ALIVE = "keep-alive"
    const val CHUNKED = "chunked"
  }
}