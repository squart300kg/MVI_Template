package kr.co.architecture.app.ui.theme

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
      primary = Color.White,
      secondary = Color.Black
    ),
    content = content
  )
}