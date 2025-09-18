package kr.co.architecture.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

val LocalCustomColors = staticCompositionLocalOf { colors() }

data class CustomColors(
  val border: Color,
  val selectedDivider: Color,
  val unselectedDivider: Color,
  val searchBackground: Color,
)

private fun colors() = CustomColors(
  border = parseColor("#E6E6E6"),
  selectedDivider = parseColor("#222222"),
  unselectedDivider = parseColor("#DDDDDD"),
  searchBackground = parseColor("#F7F7F7")
)

internal fun parseColor(hex: String) = Color(hex.toColorInt())
