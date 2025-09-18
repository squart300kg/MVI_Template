package kr.co.architecture.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalCustomShapes = staticCompositionLocalOf { shapes() }

data class CustomShapes(
  val shapeDp: Dp,
) {
  val shape: RoundedCornerShape
    get() = RoundedCornerShape(shapeDp)
}

private fun shapes() = CustomShapes(
  shapeDp = 14.dp
)