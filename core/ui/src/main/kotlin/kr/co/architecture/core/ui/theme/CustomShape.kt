package kr.co.architecture.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val LocalCustomShapes = staticCompositionLocalOf { shapes() }

data class CustomShapes(
  val shape: RoundedCornerShape,
)

private fun shapes() = CustomShapes(
  shape = RoundedCornerShape(14.dp)
)