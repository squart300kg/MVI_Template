package kr.co.architecture.custom.image.loader.domain.mediator

import android.content.Context
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.AGE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.CACHE_CONTROL
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.IMMUTABLE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.MAX_AGE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.MUST_REVALIDATE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.NO_CACHE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.NO_STORE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.STALE_IF_ERROR
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Value.STALE_WHILE_REVALIDATE
import kr.co.architecture.custom.http.client.model.toBytes
import kr.co.architecture.custom.image.loader.domain.mediator.ImageDiskCache.DiskEntry
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Formatter._BIN
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Formatter._META
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Formatter._TEMP
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_IMMUTABLE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.CC_MAX_AGE
import kr.co.architecture.custom.image.loader.domain.model.DiskCacheConstants.Key.LAST_MODIFIED
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
import kr.co.architecture.custom.image.loader.util.hashSha256
import java.io.File
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

  override fun getCachedEntry(url: String): DiskEntry? {
    val dataFile = File(directory, "${hashSha256(url)}$_BIN")
    val metaFile = File(directory, "${hashSha256(url)}$_META")
    if (!dataFile.exists() || !metaFile.exists()) return null

    val meta = readMeta(metaFile) ?: return null

    val bytes = runCatching { dataFile.readBytes() }.getOrNull() ?: return null

    System.currentTimeMillis().also { now ->
      dataFile.setLastModified(now)
      metaFile.setLastModified(now)
    }

    return DiskEntry(
      bytes = bytes.toBytes(),
      meta = meta
    )
  }

  override fun cacheBodyAndMeta(url: String, body: ByteArray, header: Map<String, String>) {
    val policy = parseCacheControl(header[CACHE_CONTROL]) ?: return
    if (policy.noStore) return

    val now = System.currentTimeMillis()
    val age = header[AGE]?.toLongOrNull() ?: 0L
    val expiresAt = when {
      policy.immutable -> Long.MAX_VALUE
      policy.maxAgeSeconds != null -> now + maxOf(0, policy.maxAgeSeconds - age) * 1000
      else -> null // 명시적 만료 없음 → 항상 재검증 대상으로 취급
    }

    writeAtomic(
      url = url,
      bytes = body,
      meta = Meta(
        storedAtMillis = now,
        expiresAtMillis = expiresAt,
        etag = header[ETAG],
        lastModified = header[LAST_MODIFIED],
        policy = policy
      )
    )
    removeByLru()
  }

  override fun cacheMeta(url: String, header: Map<String, String>) {
    val metaFile = File(directory, "${hashSha256(url)}$_META")
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
    File(directory, "${hashSha256(url)}$_BIN").setLastModified(now)
    metaFile.setLastModified(now)
  }

  private fun writeAtomic(url: String, bytes: ByteArray, meta: Meta) {
    val base = hashSha256(url)
    val dataTmp = File(directory, "$base$_BIN$_TEMP")
    val metaTmp = File(directory, "$base$_META$_TEMP")
    val dataDst = File(directory, "$base$_BIN")
    val metaDst = File(directory, "$base$_META")

    runCatching {
      dataTmp.outputStream().use { it.write(bytes) }
      writeMeta(metaTmp, meta)

      // 원자적 교체
      if (!dataTmp.renameTo(dataDst)) {
        dataDst.delete()
        dataTmp.renameTo(dataDst)
      }
      if (!metaTmp.renameTo(metaDst)) {
        metaDst.delete()
        metaTmp.renameTo(metaDst)
      }
      System.currentTimeMillis().also { now ->
        dataDst.setLastModified(now)
        metaDst.setLastModified(now)
      }
      removeByLru()
    }.onFailure {
      dataTmp.delete()
      metaTmp.delete()
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
        token.startsWith("$STALE_WHILE_REVALIDATE=") -> swr =
          token.substringAfter('=').toLongOrNull()
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
    file
      .inputStream()
      .use { fileInputStream -> Properties().apply { load(fileInputStream) } }
      .run {
        Meta(
          storedAtMillis = getProperty(STORED_AT).toLong(),
          expiresAtMillis = getProperty(EXPIRES_AT)?.toLong(),
          etag = getProperty(ETAG),
          lastModified = getProperty(LAST_MODIFIED),
          policy = CachePolicy(
            noStore = getProperty(CC_NO_STORE) == TRUE,
            noCache = getProperty(CC_NO_CACHE) == TRUE,
            mustRevalidate = getProperty(CC_MUST_REVALIDATE) == TRUE,
            immutable = getProperty(CC_IMMUTABLE) == TRUE,
            maxAgeSeconds = getProperty(CC_MAX_AGE)?.toLong(),
            staleWhileRevalidateSeconds = getProperty(CC_STALE_WHILE_REVALIDATE)?.toLong(),
            staleIfErrorSeconds = getProperty(CC_STALE_IF_ERROR)?.toLong()
          )
        )
      }
  }.getOrNull()

  private fun writeMeta(file: File, meta: Meta) {
    file
      .outputStream()
      .buffered()
      .use { out ->
        Properties().apply {
          setProperty(STORED_AT, meta.storedAtMillis.toString())
          meta.expiresAtMillis?.let { setProperty(EXPIRES_AT, it.toString()) }
          meta.etag?.let { setProperty(ETAG, it) }
          meta.lastModified?.let { setProperty(LAST_MODIFIED, it) }
          meta.policy.maxAgeSeconds?.let { setProperty(CC_MAX_AGE, it.toString()) }
          meta.policy.staleWhileRevalidateSeconds?.let { setProperty(CC_STALE_WHILE_REVALIDATE, it.toString()) }
          meta.policy.staleIfErrorSeconds?.let { setProperty(CC_STALE_IF_ERROR, it.toString()) }
          setProperty(CC_NO_STORE, if (meta.policy.noStore) TRUE else FALSE)
          setProperty(CC_NO_CACHE, if (meta.policy.noCache) TRUE else FALSE)
          setProperty(CC_MUST_REVALIDATE, if (meta.policy.mustRevalidate) TRUE else FALSE)
          setProperty(CC_IMMUTABLE, if (meta.policy.immutable) TRUE else FALSE)
        }.store(out, null)
      }
  }

  private fun removeByLru() {
    val files = directory.listFiles().orEmpty()
    var total = files.sumOf(File::length)
    if (total <= maxBytes) return

    // 오래된 파일부터 (data/meta 쌍을 함께 제거)
    val grouped = files.groupBy { it.nameWithoutExtension.substringBefore('.') }
    val sorted = grouped.values
      .mapNotNull { group -> group.maxByOrNull(File::lastModified) } // 대표(최신 mtime) 선택
      .sortedBy(File::lastModified)

    for (rep in sorted) {
      if (total <= maxBytes) break
      val base = rep.nameWithoutExtension.substringBefore('.')
      val data = File(directory, "$base$_BIN")
      val meta = File(directory, "$base$_META")

      val reduced =
        (data.takeIf { it.exists() }?.length() ?: 0L) +
          (meta.takeIf { it.exists() }?.length() ?: 0L)

      data.delete()
      meta.delete()
      total -= reduced
    }
  }

}
