package kr.co.architecture.custom.image.loader.domain.mediator

import android.graphics.Bitmap
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap


object ImageMemoryCache {
  private const val MAX_BYTES = 16 * 1024 * 1024

  private val cache = object : LruCache<String, Bitmap>(MAX_BYTES) {
    override fun sizeOf(key: String, value: Bitmap): Int = value.allocationByteCount
  }
  // TODO: 여러 메서드 오버라이드해서 메모리 관리

  @Synchronized
  operator fun get(key: String): ImageBitmap? =
    cache.get(key)?.asImageBitmap()

  @Synchronized
  fun put(key: String, image: ImageBitmap) {
    cache.put(key, image.asAndroidBitmap())
  }
}