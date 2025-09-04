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
import kr.co.architecture.custom.image.loader.domain.mediator.ImageDiskCache
import kr.co.architecture.custom.image.loader.domain.mediator.ImageMemoryCache
import kr.co.architecture.custom.image.loader.domain.mediator.ImageMediator
import kr.co.architecture.custom.image.loader.network.HttpClientImpl

// ui/AsyncImage.kt
@Composable
fun AsyncImage(
  modifier: Modifier = Modifier,
  placeholderContent: @Composable () -> Unit = {},
  url: String,
  contentScale: ContentScale = ContentScale.Crop,
  context: Context = LocalContext.current
) {
  // 1) 네트워크 클라이언트 + 디스크/메모리 캐시 준비
  val rawClient = remember {
    RawHttp11Client(
      userAgent = "Custom-Image-Loader-RawHttp11",
      httpLogger = CustomHttpLogger()
    )
  }
  val httpClient = remember(rawClient) { HttpClientImpl(rawClient) }
  val diskCache = remember {
    ImageDiskCache.create(context, maxBytes = 64L * 1024 * 1024)
  }
  val engine = remember {
    ImageMediator(
      imageMemoryCache = ImageMemoryCache,
      imageDiskCache = diskCache,
      httpClient = httpClient
    )
  }

  // 2) 엔진 스트림 수집 → 상태 표시
  val imageBitmap by remember(url) {
    engine.imageFlow(url)
  }.collectAsState(initial = null)

  imageBitmap?.let { img ->
    Image(
      modifier = modifier,
      bitmap = img,
      contentDescription = null,
      contentScale = contentScale
    )
  } ?: placeholderContent()
}
