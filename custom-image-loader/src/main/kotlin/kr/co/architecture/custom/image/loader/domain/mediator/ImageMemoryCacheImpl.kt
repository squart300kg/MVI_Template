package kr.co.architecture.custom.image.loader.domain.mediator

import android.graphics.Bitmap
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

private const val LRU_CACHE_NATIVE_HEAP_MAX_SIZE = Int.MAX_VALUE

class ImageMemoryCacheImpl private constructor(
) : ImageMemoryCache {

  companion object {
    @Volatile
    private var INSTANCE: ImageMemoryCache? = null

    @JvmStatic
    fun getInstance(): ImageMemoryCache {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: ImageMemoryCacheImpl().also {
          INSTANCE = it
        }
      }
    }
  }

  @Synchronized
  override fun get(key: String): ImageBitmap? =
    lruCache.get(key)?.asImageBitmap()

  @Synchronized
  override fun cache(key: String, image: ImageBitmap) {
    val imageBitmap = image.asAndroidBitmap()
    val imageBitmapSize = imageBitmap.allocationByteCount

    // LruCache의 네이티브 힙 메모리 최대 상한(2GB)를 넘지 않도록 trim
    val targetBeforePut = (LRU_CACHE_NATIVE_HEAP_MAX_SIZE - imageBitmapSize).coerceAtLeast(0)
    if (lruCache.size() > targetBeforePut) {
      lruCache.trimToSize(targetBeforePut)
    }

    lruCache.put(key, imageBitmap)
  }

  private val lruCache = object : LruCache<String, Bitmap>(LRU_CACHE_NATIVE_HEAP_MAX_SIZE) {
    override fun sizeOf(key: String, value: Bitmap): Int = value.allocationByteCount
  }

  private fun getAvailableHeapMemory(minEmergencyBytes: Long = 1L * 1024 * 1024): Int {
    val (appMaxMemory, nowUsedMemory) = with (Runtime.getRuntime()) {
      maxMemory() to totalMemory() - freeMemory()
    }
    val headroom = (appMaxMemory - nowUsedMemory - minEmergencyBytes).coerceAtLeast(0L)
    // LruCache 용량 최대 타입에 맞게 변환
    return headroom.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
  }
}
