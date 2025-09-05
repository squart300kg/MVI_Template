package kr.co.architecture.custom.image.loader.domain.model

object DiskCacheConstants {
  object Key {
    const val STORED_AT = "storedAt"
    const val EXPIRES_AT = "expiresAt"
    const val ETAG = "etag"
    const val LAST_MODIFIED = "lastModified"
    const val CC_NO_STORE = "cc.noStore"
    const val CC_NO_CACHE = "cc.noCache"
    const val CC_MUST_REVALIDATE = "cc.mustRevalidate"
    const val CC_IMMUTABLE = "cc.immutable"
    const val CC_MAX_AGE = "cc.maxAge"
    const val CC_STALE_WHILE_REVALIDATE = "cc.swr"
    const val CC_STALE_IF_ERROR = "cc.sie"
  }

  object Value {
    const val TRUE = "1"
    const val FALSE = "0"
  }

  object Formatter {
    const val _BIN = ".bin"
    const val _META = ".meta"
    const val _TEMP = ".temp"
  }

}