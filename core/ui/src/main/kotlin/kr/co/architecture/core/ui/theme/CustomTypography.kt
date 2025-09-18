package kr.co.architecture.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
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
  val tab: TextStyle,
  val tabMedium: TextStyle,
  val searchContents: TextStyle,
  val searchMedium: TextStyle,
)

private fun typography(fontFamily: FontFamily = defaultFont) = CustomTypography(
  title = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 15.sp,
    color = parseColor("#000000")
  ),
  titleMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = parseColor("#465179")
  ),
  contents = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    color = parseColor("#444444")
  ),
  contentsMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    color = parseColor("#888888")
  ),
  tab = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    color = parseColor("#222222")
  ),
  tabMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = parseColor("#888888")
  ),
  searchContents = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = parseColor("#222222")
  ),
  searchMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = Color.LightGray
  ),

)

private val defaultFont: FontFamily by lazy {
  FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Light),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.Bold)
  )
}
