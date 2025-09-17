package kr.co.architecture.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

val LocalCustomColors = staticCompositionLocalOf { colors() }

data class CustomColors(
  val title: Color,
  val titleVariant: Color,
  val primaryContents: Color,
  val secondaryContents: Color,
  val border: Color,
  val divider: Color
)

private fun colors() = CustomColors(
  title = parseColor("#000000"),
  titleVariant = parseColor("#465179"),
  primaryContents = parseColor("#444444"),
  secondaryContents = parseColor("#888888"),
  border = parseColor("#E6E6E6"),
  divider = parseColor("#000000"),
)
private fun parseColor(hex: String) = Color(hex.toColorInt())
