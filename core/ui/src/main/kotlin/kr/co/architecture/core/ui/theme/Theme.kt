package kr.co.architecture.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
    content = {
      Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
      ) {
        content()
      }
    }
  )
}
