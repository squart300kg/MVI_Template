package kr.co.architecture.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kr.co.architecture.core.ui.theme.LocalCustomColors
import kr.co.architecture.core.ui.theme.LocalCustomShapes

@Composable
fun CoilAsyncImage(
  modifier: Modifier = Modifier,
  url: String,
  contentDescription: String? = null
) {
  val shapes = LocalCustomShapes.current
  val colors = LocalCustomColors.current
  val density = LocalDensity.current
  val context = LocalContext.current
  val roundPx = with(density) { shapes.shapeDp.toPx() }
  val request = ImageRequest.Builder(context)
    .data(url)
    .crossfade(true)
    .memoryCacheKey(url)
    .diskCacheKey(url)
    .dispatcher(Dispatchers.IO)
    .allowHardware(true)
    .transformations(RoundedCornersTransformation(roundPx))
    .build()

  Box(
    modifier = modifier
      .clip(shapes.shape)
      .border(
        width = 1.dp,
        color = colors.border,
        shape = shapes.shape
      )
  ) {
    AsyncImage(
      model = request,
      placeholder = ColorPainter(colors.border),
      error = painterResource(R.drawable.thumbnail_default),
      contentScale = ContentScale.Crop,
      contentDescription = contentDescription
    )
  }
}