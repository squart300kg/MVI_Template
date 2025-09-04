package kr.co.architecture.custom.image.loader.domain.model

data class Meta(
  val storedAtMillis: Long,
  val expiresAtMillis: Long?,
  val etag: String?,
  val lastModified: String?,
  val policy: CachePolicy
) {
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