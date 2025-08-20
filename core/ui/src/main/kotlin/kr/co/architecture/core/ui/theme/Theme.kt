package kr.co.architecture.core.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BaseTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    content = {
      Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
      ) {
        content()
      }

    }
  )
}