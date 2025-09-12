package kr.co.architecture.yeo.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun NoResultContent(modifier: Modifier = Modifier) {
  Box(modifier.fillMaxSize()) {
    Text(
      modifier = Modifier.align(Alignment.Center),
      text = stringResource(R.string.noResult)
    )
  }
}