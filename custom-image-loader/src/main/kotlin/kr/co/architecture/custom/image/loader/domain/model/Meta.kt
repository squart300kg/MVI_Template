package kr.co.architecture.custom.image.loader.domain.model

data class Meta(
  val storedAtMillis: Long,
  val expiresAtMillis: Long?,
  val etag: String?,
  val lastModified: String?,
  val policy: CachePolicy
) {
  data class CachePolicy(
    val noStore: Boolean = false,
    val noCache: Boolean = false,
    val mustRevalidate: Boolean = false,
    val immutable: Boolean = false,
    val maxAgeSeconds: Long? = null,
    val staleWhileRevalidateSeconds: Long? = null,
    val staleIfErrorSeconds: Long? = null
  )
  fun isFresh(now: Long = System.currentTimeMillis()): Boolean {
    if (policy.noCache || policy.mustRevalidate) return false
    if (expiresAtMillis == Long.MAX_VALUE) return true
    return expiresAtMillis?.let { now < it } ?: false
  }

  fun canServeStaleWhileRevalidate(now: Long = System.currentTimeMillis()): Boolean {
    val exp = expiresAtMillis ?: return false
    val swr = policy.staleWhileRevalidateSeconds ?: return false
    // 만료 이후(exp ≤ now)이고, SWR 윈도우 내
    return now >= exp && now < exp + swr * 1000
  }

  fun canServeStaleOnError(now: Long = System.currentTimeMillis()): Boolean {
    val exp = expiresAtMillis ?: return false
    val sie = policy.staleIfErrorSeconds ?: 0
    // 만료 이후(exp ≤ now)이고, SiE 윈도우 내
    return now >= exp && now < exp + sie * 1000
  }
}