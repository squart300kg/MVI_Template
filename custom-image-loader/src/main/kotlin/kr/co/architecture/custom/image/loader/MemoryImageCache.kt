package kr.co.architecture.custom.image.loader

import android.graphics.Bitmap
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap


object MemoryImageCache {
  private const val MAX_BYTES = 16 * 1024 * 1024

  private val cache = object : LruCache<String, Bitmap>(MAX_BYTES) {
    override fun sizeOf(key: String, value: Bitmap): Int = value.allocationByteCount
  }
  // TODO: 여러 메서드 오버라이드해서 메모리 관리

  @Synchronized
  operator fun get(key: String): ImageBitmap? =
    cache.get(key)?.asImageBitmap()

  @Synchronized
  fun getBitmap(key: String): Bitmap? =
    cache.get(key)

  @Synchronized
  fun put(key: String, bitmap: Bitmap) {
    cache.put(key, bitmap)
  }

  @Synchronized
  fun put(key: String, image: ImageBitmap) {
    cache.put(key, image.asAndroidBitmap())
  }

  @Synchronized
  fun clear() = cache.evictAll()
}