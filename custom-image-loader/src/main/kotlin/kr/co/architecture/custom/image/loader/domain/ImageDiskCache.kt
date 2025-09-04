package kr.co.architecture.custom.image.loader.domain

import android.content.Context
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CACHE_CONTROL
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.ETAG
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.LAST_MODIFIED
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.AGE
import kr.co.architecture.custom.image.loader.domain.model.CachePolicy
import java.io.*
import java.security.MessageDigest
import java.util.Properties
import java.util.Locale

// --------- Cache-Control 모델 ----------

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

data class DiskEntry(val bytes: ByteArray, val meta: Meta)

class ImageDiskCache private constructor(
  private val directory: File,
  private val maxBytes: Long
) {

  companion object {
    fun create(
      context: Context,
      subDirectory: String = "gallery-app-disk-cache",
      maxBytes: Long = 64L * 1024 * 1024
    ): ImageDiskCache {
      val file = File(context.cacheDir, subDirectory).apply { mkdirs() }
      return ImageDiskCache(file, maxBytes)
    }
  }

  // ---- 외부에 노출하는 API ----
  fun getEntry(url: String): DiskEntry? {
    val data = dataFile(url)
    val meta = metaFile(url)
    if (!data.exists() || !meta.exists()) return null
    val m = readMeta(meta) ?: return null
    val bytes = runCatching { data.readBytes() }.getOrNull() ?: return null
    val now = System.currentTimeMillis()
    data.setLastModified(now)
    meta.setLastModified(now)
    return DiskEntry(bytes, m)
  }

  fun putHttpResponse(url: String, body: ByteArray, header: Map<String, String>) {
    val policy = parseCacheControl(header[CACHE_CONTROL])
    if (policy.noStore) return // 저장 금지

    val now = System.currentTimeMillis()
    val age = header["age"]?.toLongOrNull() ?: 0L
    val expiresAt = when {
      policy.immutable -> Long.MAX_VALUE
      policy.maxAgeSeconds != null -> now + maxOf(0, policy.maxAgeSeconds - age) * 1000
      else -> null // 명시적 만료 없음 → 항상 재검증 대상으로 취급
    }
    val meta = Meta(
      // TODO: String들 상수로 정의
      storedAtMillis = now,
      expiresAtMillis = expiresAt,
      etag = header[ETAG],
      lastModified = header[LAST_MODIFIED],
      policy = policy
    )
    writeAtomic(url, body, meta)
    evictIfNeeded()
  }

  fun updateMetaOn304(url: String, header: Map<String, String>) {
    val metaFile = metaFile(url)
    val old = readMeta(metaFile) ?: return
    val policy = parseCacheControl(header[CACHE_CONTROL]) // 304에도 새 CC가 올 수 있음
    val now = System.currentTimeMillis()
    val age = header[AGE]?.toLongOrNull() ?: 0L

    val newExpiresAt = when {
      policy.noStore -> null
      policy.immutable -> Long.MAX_VALUE
      policy.maxAgeSeconds != null -> now + maxOf(0, policy.maxAgeSeconds - age) * 1000
      else -> old.expiresAtMillis
    }

    val newMeta = old.copy(
      storedAtMillis = now,
      expiresAtMillis = newExpiresAt,
      etag = header[ETAG] ?: old.etag,
      lastModified = header[LAST_MODIFIED] ?: old.lastModified,
      policy = if (header[CACHE_CONTROL] != null) policy else old.policy
    )
    // TODO: CRUD하는 로직 DataLayer로 분리
    writeMeta(metaFile, newMeta)
    // LRU touch
    dataFile(url).setLastModified(now)
    metaFile.setLastModified(now)
  }

  // ---- 내부 구현 ----
  private fun writeAtomic(url: String, bytes: ByteArray, meta: Meta) {
    val base = hash(url)
    val dataTmp = File(directory, "$base.bin.tmp")
    val metaTmp = File(directory, "$base.meta.tmp")
    val dataDst = File(directory, "$base.bin")
    val metaDst = File(directory, "$base.meta")

    runCatching {
      FileOutputStream(dataTmp).use { it.write(bytes) }
      writeMeta(metaTmp, meta)

      // 원자적 교체
      if (!dataTmp.renameTo(dataDst)) { dataDst.delete(); dataTmp.renameTo(dataDst) }
      if (!metaTmp.renameTo(metaDst)) { metaDst.delete(); metaTmp.renameTo(metaDst) }
      val now = System.currentTimeMillis()
      dataDst.setLastModified(now)
      metaDst.setLastModified(now)
      evictIfNeeded()
    }.onFailure {
      dataTmp.delete(); metaTmp.delete()
    }
  }

  private fun parseCacheControl(header: String?): CachePolicy {
    if (header.isNullOrBlank()) return CachePolicy()
    var noStore = false
    var noCache = false
    var mustRevalidate = false
    var immutable = false
    var maxAge: Long? = null
    var swr: Long? = null
    var sie: Long? = null

    header.split(',').forEach { raw ->
      val token = raw.trim().lowercase(Locale.ROOT)
      when {
        token == "no-store" -> noStore = true
        token == "no-cache" -> noCache = true
        token == "must-revalidate" -> mustRevalidate = true
        token == "immutable" -> immutable = true
        token.startsWith("max-age=") -> maxAge = token.substringAfter('=').toLongOrNull()
        token.startsWith("stale-while-revalidate=") -> swr = token.substringAfter('=').toLongOrNull()
        token.startsWith("stale-if-error=") -> sie = token.substringAfter('=').toLongOrNull()
      }
    }
    return CachePolicy(noStore, noCache, mustRevalidate, immutable, maxAge, swr, sie)
  }

  private fun readMeta(file: File): Meta? = runCatching {
    Properties().useAndLoad(file).let { property ->
      Meta(
        storedAtMillis = property.getProperty("storedAt").toLong(),
        expiresAtMillis = property.getProperty("expiresAt")?.toLong(),
        etag = property.getProperty("etag"),
        lastModified = property.getProperty("lastModified"),
        policy = CachePolicy(
          noStore = property.getProperty("cc.noStore") == "1",
          noCache = property.getProperty("cc.noCache") == "1",
          mustRevalidate = property.getProperty("cc.mustRevalidate") == "1",
          immutable = property.getProperty("cc.immutable") == "1",
          maxAgeSeconds = property.getProperty("cc.maxAge")?.toLong(),
          staleWhileRevalidateSeconds = property.getProperty("cc.swr")?.toLong(),
          staleIfErrorSeconds = property.getProperty("cc.sie")?.toLong()
        )
      )
    }
  }.getOrNull()

  private fun writeMeta(file: File, meta: Meta) {
    val p = Properties().apply {
      setProperty("storedAt", meta.storedAtMillis.toString())
      meta.expiresAtMillis?.let { setProperty("expiresAt", it.toString()) }
      meta.etag?.let { setProperty("etag", it) }
      meta.lastModified?.let { setProperty("lastModified", it) }
      setProperty("cc.noStore", if (meta.policy.noStore) "1" else "0")
      setProperty("cc.noCache", if (meta.policy.noCache) "1" else "0")
      setProperty("cc.mustRevalidate", if (meta.policy.mustRevalidate) "1" else "0")
      setProperty("cc.immutable", if (meta.policy.immutable) "1" else "0")
      meta.policy.maxAgeSeconds?.let { setProperty("cc.maxAge", it.toString()) }
      meta.policy.staleWhileRevalidateSeconds?.let { setProperty("cc.swr", it.toString()) }
      meta.policy.staleIfErrorSeconds?.let { setProperty("cc.sie", it.toString()) }
    }
    FileOutputStream(file).use { out -> p.store(out, null) }
  }

  private fun Properties.useAndLoad(file: File): Properties {
    FileInputStream(file).use { load(it) }
    return this
  }

  private fun fileBase(url: String) = hash(url)
  private fun dataFile(url: String) = File(directory, "${fileBase(url)}.bin")
  private fun metaFile(url: String) = File(directory, "${fileBase(url)}.meta")

  private fun hash(s: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(s.toByteArray(Charsets.UTF_8))
    val sb = StringBuilder(bytes.size * 2)
    for (b in bytes) {
      val v = b.toInt() and 0xFF
      if (v < 16) sb.append('0')
      sb.append(Integer.toHexString(v))
    }
    return sb.toString()
  }

  private fun evictIfNeeded() {
    val files = directory.listFiles() ?: return
    var total = files.sumOf { it.length() }
    if (total <= maxBytes) return

    // 오래된 파일부터(data/meta 쌍을 함께 제거)
    val grouped = files.groupBy { it.nameWithoutExtension.substringBefore('.') } // base 구하기
    val sorted = grouped.values
      .map { it.maxByOrNull { f -> f.lastModified() }!! } // 대표(최신 mtime) 선택
      .sortedBy { it.lastModified() }

    var i = 0
    while (total > maxBytes && i < sorted.size) {
      val base = sorted[i++].nameWithoutExtension.substringBefore('.')
      val ds = File(directory, "$base.bin")
      val ms = File(directory, "$base.meta")
      total -= (ds.length() + ms.length())
      ds.delete(); ms.delete()
    }
  }
}
