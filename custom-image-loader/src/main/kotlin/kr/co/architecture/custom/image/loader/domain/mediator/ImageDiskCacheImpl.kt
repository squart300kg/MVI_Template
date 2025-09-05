package kr.co.architecture.custom.image.loader.domain.mediator

import android.content.Context
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.AGE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CACHE_CONTROL
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.LAST_MODIFIED
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.IMMUTABLE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.MAX_AGE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.MUST_REVALIDATE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.NO_CACHE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.NO_STORE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.STALE_IF_ERROR
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.STALE_WHILE_REVALIDATE
import kr.co.architecture.custom.image.loader.domain.mediator.ImageDiskCache.DiskEntry
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Formatter._BIN
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Formatter._META
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Formatter._TEMP
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_IMMUTABLE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_MAX_AGE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_MUST_REVALIDATE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_NO_CACHE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_NO_STORE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_STALE_IF_ERROR
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_STALE_WHILE_REVALIDATE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.ETAG
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.EXPIRES_AT
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.STORED_AT
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Value.FALSE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Value.TRUE
import kr.co.architecture.custom.image.loader.domain.model.Meta
import kr.co.architecture.custom.image.loader.domain.model.Meta.CachePolicy
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.Locale
import java.util.Properties

class ImageDiskCacheImpl private constructor(
  context: Context,
  subDirectory: String,
  private val maxBytes: Long = 64L * 1024 * 1024
) : ImageDiskCache {

  companion object {
    @Volatile
    private var INSTANCE: ImageDiskCache? = null

    @JvmStatic
    fun getInstance(
      context: Context,
      subDirectory: String = "gallery-app-disk-cache",
      maxBytes: Long = 64L * 1024 * 1024
    ): ImageDiskCache {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: ImageDiskCacheImpl(
          context = context.applicationContext,
          subDirectory = subDirectory,
          maxBytes = maxBytes
        ).also { INSTANCE = it }
      }
    }
  }

  private var directory =
    File(context.cacheDir, subDirectory).apply { mkdirs() }

  override fun getEntry(url: String): DiskEntry? {
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

  override fun putHttpResponse(url: String, body: ByteArray, header: Map<String, String>) {
    val policy = parseCacheControl(header[CACHE_CONTROL]) ?: return
    if (policy.noStore) return // 저장 금지

    val now = System.currentTimeMillis()
    val age = header[AGE]?.toLongOrNull() ?: 0L
    val expiresAt = when {
      policy.immutable -> Long.MAX_VALUE
      policy.maxAgeSeconds != null -> now + maxOf(0, policy.maxAgeSeconds - age) * 1000
      else -> null // 명시적 만료 없음 → 항상 재검증 대상으로 취급
    }
    val meta = Meta(
      storedAtMillis = now,
      expiresAtMillis = expiresAt,
      etag = header[ETAG],
      lastModified = header[LAST_MODIFIED],
      policy = policy
    )
    writeAtomic(url, body, meta)
    evictIfNeeded()
  }

  override fun updateMetaOn304(url: String, header: Map<String, String>) {
    val metaFile = metaFile(url)
    val old = readMeta(metaFile) ?: return
    val policy = parseCacheControl(header[CACHE_CONTROL]) ?: return
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
    writeMeta(metaFile, newMeta)
    dataFile(url).setLastModified(now)
    metaFile.setLastModified(now)
  }

  private fun writeAtomic(url: String, bytes: ByteArray, meta: Meta) {
    val base = hash(url)
    val dataTmp = File(directory, "$base$_BIN$_TEMP")
    val metaTmp = File(directory, "$base$_META$_TEMP")
    val dataDst = File(directory, "$base$_BIN")
    val metaDst = File(directory, "$base$_META")

    runCatching {
      FileOutputStream(dataTmp).use { it.write(bytes) }
      writeMeta(metaTmp, meta)

      // 원자적 교체
      if (!dataTmp.renameTo(dataDst)) {
        dataDst.delete(); dataTmp.renameTo(dataDst)
      }
      if (!metaTmp.renameTo(metaDst)) {
        metaDst.delete(); metaTmp.renameTo(metaDst)
      }
      val now = System.currentTimeMillis()
      dataDst.setLastModified(now)
      metaDst.setLastModified(now)
      evictIfNeeded()
    }.onFailure {
      dataTmp.delete(); metaTmp.delete()
    }
  }

  private fun parseCacheControl(header: String?): CachePolicy? {
    if (header.isNullOrBlank()) return null
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
        token == NO_STORE -> noStore = true
        token == NO_CACHE -> noCache = true
        token == MUST_REVALIDATE -> mustRevalidate = true
        token == IMMUTABLE -> immutable = true
        token.startsWith("$MAX_AGE=") -> maxAge = token.substringAfter('=').toLongOrNull()
        token.startsWith("$STALE_WHILE_REVALIDATE=") -> swr = token.substringAfter('=').toLongOrNull()
        token.startsWith("$STALE_IF_ERROR=") -> sie = token.substringAfter('=').toLongOrNull()
      }
    }
    return CachePolicy(
      noStore = noStore,
      noCache = noCache,
      mustRevalidate = mustRevalidate,
      immutable = immutable,
      maxAgeSeconds = maxAge,
      staleWhileRevalidateSeconds = swr,
      staleIfErrorSeconds = sie
    )
  }

  private fun readMeta(file: File): Meta? = runCatching {
    Properties().useAndLoad(file).let { property ->
      Meta(
        storedAtMillis = property.getProperty(STORED_AT).toLong(),
        expiresAtMillis = property.getProperty(EXPIRES_AT)?.toLong(),
        etag = property.getProperty(ETAG),
        lastModified = property.getProperty(LAST_MODIFIED),
        policy = CachePolicy(
          noStore = property.getProperty(CC_NO_STORE) == TRUE,
          noCache = property.getProperty(CC_NO_CACHE) == TRUE,
          mustRevalidate = property.getProperty(CC_MUST_REVALIDATE) == TRUE,
          immutable = property.getProperty(CC_IMMUTABLE) == TRUE,
          maxAgeSeconds = property.getProperty(CC_MAX_AGE)?.toLong(),
          staleWhileRevalidateSeconds = property.getProperty(CC_STALE_WHILE_REVALIDATE)?.toLong(),
          staleIfErrorSeconds = property.getProperty(CC_STALE_IF_ERROR)?.toLong()
        )
      )
    }
  }.getOrNull()

  // TODO: 상수로 정의
  private fun writeMeta(file: File, meta: Meta) {
    val property = Properties().apply {
      meta.expiresAtMillis?.let { setProperty(STORED_AT, it.toString()) }
      meta.etag?.let { setProperty(ETAG, it) }
      meta.lastModified?.let { setProperty(LAST_MODIFIED, it) }
      meta.policy.maxAgeSeconds?.let { setProperty(CC_MAX_AGE, it.toString()) }
      meta.policy.staleWhileRevalidateSeconds?.let { setProperty(CC_STALE_WHILE_REVALIDATE, it.toString()) }
      meta.policy.staleIfErrorSeconds?.let { setProperty(CC_STALE_IF_ERROR, it.toString()) }
      setProperty(STORED_AT, meta.storedAtMillis.toString())
      setProperty(CC_NO_STORE, if (meta.policy.noStore) TRUE else FALSE)
      setProperty(CC_NO_CACHE, if (meta.policy.noCache) TRUE else FALSE)
      setProperty(CC_MUST_REVALIDATE, if (meta.policy.mustRevalidate) TRUE else FALSE)
      setProperty(CC_IMMUTABLE, if (meta.policy.immutable) TRUE else FALSE)
    }
    FileOutputStream(file).use { property.store(it, null) }
  }

  private fun Properties.useAndLoad(file: File): Properties {
    FileInputStream(file).use { load(it) }
    return this
  }

  private fun fileBase(url: String) = hash(url)
  private fun dataFile(url: String) = File(directory, "${fileBase(url)}$_BIN")
  private fun metaFile(url: String) = File(directory, "${fileBase(url)}$_META")

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
      // TODO: 강제언랩핑 제거
      .map { it.maxByOrNull { f -> f.lastModified() }!! } // 대표(최신 mtime) 선택
      .sortedBy { it.lastModified() }

    var i = 0
    while (total > maxBytes && i < sorted.size) {
      val base = sorted[i++].nameWithoutExtension.substringBefore('.')
      val ds = File(directory, "$base$_BIN")
      val ms = File(directory, "$base$_META")
      total -= (ds.length() + ms.length())
      ds.delete(); ms.delete()
    }
  }
}
