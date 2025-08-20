package kr.co.architecture.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BaseTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = lightColorScheme(
      background = Color.White,
    ),
    content = content
  )
}