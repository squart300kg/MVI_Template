package kr.co.architecture.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kr.co.architecture.core.ui.R

// TODO: 이걸 staticCompositionLocalOf vs compositionLocalOf ??
val LocalCustomTypography = staticCompositionLocalOf { typography() }

data class CustomTypography(
  val title: TextStyle,
  val titleMedium: TextStyle,
  val contents: TextStyle,
  val contentsMedium: TextStyle,
  val label: TextStyle,
  val labelMedium: TextStyle,
)

private fun typography(fontFamily: FontFamily = defaultFont) = CustomTypography(
  title = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp),
  titleMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
  contents = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp),
  contentsMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
  label = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
  labelMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Light, fontSize = 16.sp),
)

private val defaultFont: FontFamily by lazy {
  FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Light),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.Bold)
  )
}
