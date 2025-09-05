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
    const val IF_NONE_MATCH = "if-none-match"
    const val IF_MODIFIED_SINCE = "if-modified-since"
    const val CACHE_CONTROL = "cache-control"
    const val ETAG = "etag"
    const val LAST_MODIFIED = "last-modified"
    const val AGE = "age"
  }
  object Value {
    const val APPLICATION_JSON = "application/json"
    const val GZIP = "gzip"
    const val CLOSE = "close"
    const val KEEP_ALIVE = "keep-alive"
    const val CHUNKED = "chunked"
    const val IMAGE_ALL = "image/*"
    const val NO_STORE = "no-store"
    const val NO_CACHE = "no-cache"
    const val MUST_REVALIDATE = "must-revalidate"
    const val IMMUTABLE = "immutable"
    const val MAX_AGE = "max-age"
    const val STALE_WHILE_REVALIDATE = "stale-while-revalidate"
    const val STALE_IF_ERROR = "stale-if-error"
  }
}