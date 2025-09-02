package kr.co.architecture.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.delay

fun Modifier.baseClickable(
  delayMillis: Long = 1000L,
  onClick: () -> Unit
): Modifier = composed {
  var isClickable by remember { mutableStateOf(true) }

  LaunchedEffect(isClickable) {
    if (!isClickable) {
      delay(delayMillis)
      isClickable = true
    }
  }

  this.clickable {
    if (isClickable) {
      isClickable = false
      onClick()
    }
  }
}