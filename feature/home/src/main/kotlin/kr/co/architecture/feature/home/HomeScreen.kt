package kr.co.architecture.feature.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.architecture.core.ui.PaginationLoadEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.network.httpClient.RawHttp11Client

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
  val client = remember { RawHttp11Client() }
  val imageState by produceState<ImageBitmap?>(initialValue = null, url) {
    // 1) 메모리 캐시
    MemoryImageCache[url]?.let { value = it; return@produceState }
    // 2) 네트워크 로드 + 디코딩
    client.callApi(
      method = "GET",
      url = url,
      onResponseSuccess = {
        val bitmap = BitmapFactory.decodeByteArray(body, 0, body.size)
        val imageBitmap = bitmap.asImageBitmap()
        MemoryImageCache.put(key = url, image = imageBitmap)
        value = imageBitmap
      },
      onResponseError = { value = null },
      onResponseException = { value = null }
    )
  }

  imageState?.let {
    Image(
      modifier = modifier,
      bitmap = imageState!!,
      contentDescription = null,
      contentScale = contentScale
    )
  } ?: run {
    Box(modifier.background(Color.LightGray))
  }
}

object MemoryImageCache {
  private const val MAX_BYTES = 16 * 1024 * 1024

  private val cache = object : LruCache<String, Bitmap>(MAX_BYTES) {
    override fun sizeOf(key: String, value: Bitmap): Int = value.allocationByteCount
  }

  // ---- GET ----
  @Synchronized operator fun get(key: String): ImageBitmap? =
    cache.get(key)?.asImageBitmap()

  @Synchronized fun getBitmap(key: String): Bitmap? =
    cache.get(key)

  // ---- PUT (두 가지 모두 지원) ----
  @Synchronized fun put(key: String, bitmap: Bitmap) {
    cache.put(key, bitmap)
  }

  @Synchronized fun put(key: String, image: ImageBitmap) {
    cache.put(key, image.asAndroidBitmap())
  }

  @Synchronized fun clear() = cache.evictAll()
}