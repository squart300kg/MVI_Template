package kr.co.architecture.core.ui

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.ui.theme.TypoSubhead2

@Composable
fun BaseDialogButton(
  modifier: Modifier = Modifier,
  message: String,
  @ColorRes textColor: Int,
  @ColorRes backgroundColor: Int,
  @ColorRes borderColor: Int,
  onClickedConfirm: () -> Unit = { }
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp)
      .background(
        color = colorResource(backgroundColor),
        shape = RoundedCornerShape(4.dp)
      )
      .border(
        width = 1.dp,
        color = colorResource(borderColor),
        shape = RoundedCornerShape(4.dp)
      )
      .noRippledClickable(onClick = onClickedConfirm)
  ) {
    Text(
      modifier = Modifier
        .align(Alignment.Center),
      text = message,
      style = TypoSubhead2.copy(
        color = colorResource(textColor),
        textAlign = TextAlign.Center
      )
    )
  }
}