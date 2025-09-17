package kr.co.architecture.core.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun NoMaterial3Theme(
  colors: CustomColors = LocalCustomColors.current,
  shapes: CustomShapes = LocalCustomShapes.current,
  typography: CustomTypography = LocalCustomTypography.current,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(
    LocalCustomColors provides colors,
    LocalCustomShapes provides shapes,
    LocalCustomTypography provides typography,
  ) {
    Box(Modifier
      .padding(
        WindowInsets.systemBars
          .union(WindowInsets.ime) // systemBars + ime 인셋 결합
          .asPaddingValues()
      )) { content() }
  }
}