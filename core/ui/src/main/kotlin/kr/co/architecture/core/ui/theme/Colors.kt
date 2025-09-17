package kr.co.architecture.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

val AppColors: ColorScheme = lightColorScheme(
  primary = color("#6B4EFF"),
  onPrimary = color("#FFFFFF"),

  surface = color("#FFFFFF"),
  onSurface = color("#121316"),        // 본문 텍스트

  surfaceVariant = color("#F2F3F5"),   // 검색바/회색 컨테이너
  onSurfaceVariant = color("#6B7280"), // 보조 텍스트(날짜/플레이스홀더)

  secondaryContainer = color("#E8E3FF"),   // 칩 배경
  onSecondaryContainer = color("#221A72"), // 칩 텍스트

  outline = color("#DBDDE3")           // 썸네일 테두리/디바이더
)
private fun color(hex: String): Color = Color(hex.toColorInt())