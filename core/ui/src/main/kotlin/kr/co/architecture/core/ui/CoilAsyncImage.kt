package kr.co.architecture.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers

@Composable
fun CoilAsyncImage(
  modifier: Modifier = Modifier,
  url: String,
  contentDescription: String? = null,
) {
  val request = ImageRequest.Builder(LocalContext.current)
    .data(url)
    .crossfade(true)
    .memoryCacheKey(url)     // 동일 URL 키로 메모리 캐시
    .diskCacheKey(url)       // 디스크 캐시 키
    .dispatcher(Dispatchers.IO) // 로딩 디스패처
    .allowHardware(true)
    // 필요 시 변환
    //.transformations(RoundedCornersTransformation(24f))
    .build()

  AsyncImage(
    model = request,
    contentDescription = contentDescription,
    contentScale = ContentScale.Crop,
    modifier = modifier
  )
}