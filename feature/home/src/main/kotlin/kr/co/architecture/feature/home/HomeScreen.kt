package kr.co.architecture.feature.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.custom.http.client.RawHttp11Client
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is HomeUiSideEffect.Load -> viewModel.fetchData()
      }
    }
  }

  FirstScreen(
    modifier = modifier,
    uiState = uiState,
    onScrollToEnd = { viewModel.setEvent(HomeUiEvent.OnScrolledToEnd) }
  )
}

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  uiState: HomeUiState,
  onScrollToEnd: () -> Unit = {}
) {

  when (uiState.uiType) {
    HomeUiType.NONE -> {}
    HomeUiType.LOADED -> {
      val listState = rememberLazyGridState()
      PaginationLoadEffect(
        listState = listState,
        onScrollToEnd = onScrollToEnd,
        hasNext = uiState.hasNext
      )
      LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        state = listState
      ) {
        items(uiState.uiModels) { item ->
          Surface(
            modifier = Modifier
              .aspectRatio(1f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
            shape = RoundedCornerShape(4.dp)
          ) {
            NetImage(
              url = item.image
            )
          }
        }
      }
    }
  }
}

@Composable
fun NetImage(
  url: String,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop
) {
  val context = LocalContext.current
  val client = remember { RawHttp11Client() }
  val diskCache = remember { ImageDiskCache.create(context, maxBytes = 64L * 1024 * 1024) }

  val imageState by produceState<ImageBitmap?>(initialValue = null, url) {
    // 1) 메모리 캐시
    MemoryImageCache[url]?.let { value = it; return@produceState }

    // 2) 디스크 캐시
    val diskBytes = withContext(Dispatchers.IO) { diskCache.getBytes(url) }
    if (diskBytes != null) {
      val bitmap = BitmapFactory.decodeByteArray(diskBytes, 0, diskBytes.size)
      if (bitmap != null) {
        val imageBitmap = bitmap.asImageBitmap()
        MemoryImageCache.put(url, imageBitmap)
        value = imageBitmap
        return@produceState
      }
    }

    // 3) 네트워크 로드 + 디코딩 + 캐시 저장
    client.callApi(
      method = "GET",
      url = url,
      onResponseSuccess = {
        val bitmap = BitmapFactory.decodeByteArray(body, 0, body.size)
        if (bitmap != null) {
          val imageBitmap = bitmap.asImageBitmap()
          value = imageBitmap
          withContext(Dispatchers.IO) {
            MemoryImageCache.put(url, imageBitmap)
            diskCache.putBytes(url, body)
          }
        } else value = null
      },
      onResponseError = { value = null },
      onResponseException = { value = null }
    )
  }

  // TODO: 로딩상태 분기
  //  1. 로딩 전
  //  2. 로딩 완료
  //  3. 로딩 실패
  imageState?.let { img ->
    Image(
      modifier = modifier,
      bitmap = img,
      contentDescription = null,
      contentScale = contentScale
    )
  } ?: run {
    BaseProgressBar(true)
  }
}

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