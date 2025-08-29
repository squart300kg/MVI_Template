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
    .memoryCacheKey(url)
    .diskCacheKey(url)
    .dispatcher(Dispatchers.IO)
    .allowHardware(true)
    .build()

  AsyncImage(
    model = request,
    contentDescription = contentDescription,
    contentScale = ContentScale.Crop,
    modifier = modifier
  )
}