package kr.co.architecture.core.ui.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.noRippledClickable(
  onClick: () -> Unit = { },
  onLongClick: () -> Unit = { },
  delayMillis: Long = 1000L
): Modifier = composed {
  var isClickable by remember { mutableStateOf(true) }

  fun clickBuilder(builder: () -> Unit) {
    if (isClickable) {
      isClickable = false
      builder()
    }
  }
  LaunchedEffect(isClickable) {
    if (!isClickable) {
      delay(delayMillis)
      isClickable = true
    }
  }
  this.combinedClickable(
    onClick = { clickBuilder(onClick) },
    onLongClick = { clickBuilder(onLongClick) },
    indication = null,
    interactionSource = remember { MutableInteractionSource() }
  )
}