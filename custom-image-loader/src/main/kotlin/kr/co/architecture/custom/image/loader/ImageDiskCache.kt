package kr.co.architecture.custom.image.loader

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

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

  fun getBytes(url: String): ByteArray? {
    val file = fileFor(url)
    if (!file.exists()) return null
    // LRU 효과: 접근 시 갱신
    file.setLastModified(System.currentTimeMillis())
    return runCatching { file.readBytes() }.getOrNull()
  }

  fun putBytes(url: String, bytes: ByteArray) {
    val tempFile = File(directory, hash(url) + ".tmp")
    val destinationFile = fileFor(url)
    runCatching {
      FileOutputStream(tempFile).use { it.write(bytes) }
      if (!tempFile.renameTo(destinationFile)) {
        // 실패시 덮어쓰기
        destinationFile.delete()
        tempFile.renameTo(destinationFile)
      }
      destinationFile.setLastModified(System.currentTimeMillis())
      evictIfNeeded()
    }.onFailure {
      // 실패하면 임시파일 정리
      tempFile.delete()
    }
  }

  fun clear() {
    directory.listFiles()?.forEach { it.delete() }
  }

  private fun fileFor(url: String) = File(directory, hash(url) + ".bin")

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

    // 오래된 파일부터 삭제(LRU: lastModified 오름차순)
    val sorted = files.sortedBy { it.lastModified() }
    val now = System.currentTimeMillis()
    var i = 0
    while (total > maxBytes && i < sorted.size) {
      val f = sorted[i++]
      total -= f.length()
      // 너무 미래 시각으로 찍힌 파일 방어
      if (f.lastModified() > now + TimeUnit.MINUTES.toMillis(1)) {
        f.setLastModified(now)
      }
      f.delete()
    }
  }
}