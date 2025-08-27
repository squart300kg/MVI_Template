package kr.co.architecture.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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