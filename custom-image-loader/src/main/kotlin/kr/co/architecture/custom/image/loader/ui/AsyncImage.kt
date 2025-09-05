package kr.co.architecture.custom.image.loader.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
  val httpClient = remember(rawClient) {
    HttpClientImpl.getInstance(rawClient)
  }
  val memoryCache = remember {
    ImageMemoryCacheImpl.getInstance()
  }
  val diskCache = remember {
    ImageDiskCacheImpl.getInstance(context)
  }
  val imageMediator = remember {
    ImageMediatorImpl(
      imageMemoryCache = memoryCache,
      imageDiskCache = diskCache,
      httpClient = httpClient
    )
  }

  // 2) 엔진 스트림 수집 → 상태 표시
  val imageBitmap by remember(url) {
    imageMediator.imageFlow(url)
  }.collectAsState(initial = ImageState.Loading)

  // TODO: 상태에 따른 분기처리
  when (imageBitmap) {
    is ImageState.Success -> {
      val img = (imageBitmap as ImageState.Success).imageBitmap
      Image(
        modifier = modifier,
        bitmap = img,
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
