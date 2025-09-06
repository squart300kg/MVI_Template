package kr.co.architecture.custom.image.loader.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.architecture.custom.http.client.RawHttp11Client
import kr.co.architecture.custom.http.client.interceptor.CustomHttpLogger
import kr.co.architecture.custom.image.loader.domain.mediator.ImageDiskCacheImpl
import kr.co.architecture.custom.image.loader.domain.mediator.ImageMediatorImpl
import kr.co.architecture.custom.image.loader.domain.mediator.ImageMemoryCacheImpl
import kr.co.architecture.custom.image.loader.domain.mediator.ImageState
import kr.co.architecture.custom.image.loader.network.HttpClientImpl

@Composable
fun AsyncImage(
  modifier: Modifier = Modifier,
  enableMemoryCache: Boolean = false,
  enableDiskCache: Boolean = false,
  loadingPlaceholderContent: (@Composable () -> Unit)? = null,
  errorPlaceholderContent: (@Composable () -> Unit)? = null,
  url: String,
  contentScale: ContentScale = ContentScale.Crop,
  context: Context = LocalContext.current
) {
  // 1) 네트워크 클라이언트 + 디스크/메모리 캐시 준비
  val rawClient = remember {
    RawHttp11Client.getInstance(
      userAgent = "Custom-Image-Loader-RawHttp11",
      httpLogger = CustomHttpLogger()
    )
  }
  val httpClient =
    remember(rawClient) { HttpClientImpl.getInstance(rawClient) }

  val imageMemoryCache =
    if (enableMemoryCache) remember { AggressiveImageMemoryCacheImpl.getInstance() }
    else null

  val diskMemoryCache =
    if (enableDiskCache) remember { ImageDiskCacheImpl.getInstance(context) }
    else null

  val imageMediator = remember {
    ImageMediatorImpl(
      imageMemoryCache = imageMemoryCache,
      imageDiskCache = diskMemoryCache,
      httpClient = httpClient
    )
  }

  // 2) 엔진 스트림 수집 → 상태 표시
  val imageState by remember(url) {
    imageMediator.imageFlow(url)
  }.collectAsStateWithLifecycle(ImageState.Loading)

  when (val state = imageState) {
    is ImageState.Success -> {
      Image(
        modifier = modifier,
        bitmap = state.imageBitmap,
        contentDescription = null,
        contentScale = contentScale
      )
    }
    is ImageState.Loading -> {
      loadingPlaceholderContent
        ?.let { it() }
        ?: run { /** 기본 로딩 이미지 **/ }
    }
    is ImageState.Failure -> {
      errorPlaceholderContent
        ?.let { it() }
        ?:run { /**기본 에러 이미지**/ }
    }
  }
}
