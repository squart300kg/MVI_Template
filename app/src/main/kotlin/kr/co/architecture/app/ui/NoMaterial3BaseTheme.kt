package kr.co.architecture.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NoMaterial3BaseTheme(
  content: @Composable () -> Unit
) {
  Box(
    modifier = Modifier.padding(
      WindowInsets.systemBars
        .union(WindowInsets.ime) // systemBars + ime 인셋 결합
        .asPaddingValues()
    )
  ) { content() }
}