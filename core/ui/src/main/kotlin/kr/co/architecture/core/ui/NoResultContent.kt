package kr.co.architecture.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun NoResultContent(
  modifier: Modifier = Modifier,
  @StringRes textRes: Int
) {
  Box(modifier.fillMaxSize()) {
    BasicText(
      modifier = Modifier.align(Alignment.Center),
      text = stringResource(textRes)
    )
  }
}