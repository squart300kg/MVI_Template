package kr.co.architecture.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

fun Modifier.roundItem(
  backgroundColor: Color = Color.White,
  roundDp: Dp = 10.dp
) =
  this.background(
    color = backgroundColor,
    shape = RoundedCornerShape(roundDp)
  )
    .border(
      width = 1.dp,
      color = backgroundColor,
      shape = RoundedCornerShape(10.dp)
    )

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