package kr.co.architecture.custom.image.loader.domain.model

data class CachePolicy(
  val noStore: Boolean = false,
  val noCache: Boolean = false,
  val mustRevalidate: Boolean = false,
  val immutable: Boolean = false,
  val maxAgeSeconds: Long? = null,
  val staleWhileRevalidateSeconds: Long? = null,
  val staleIfErrorSeconds: Long? = null
)