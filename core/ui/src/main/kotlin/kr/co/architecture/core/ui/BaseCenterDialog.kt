package kr.co.architecture.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.asString

data class BaseCenterDialogUiModel(
  val titleMessage: UiText,
  val contentMessage: UiText,
  val confirmButtonMessage: UiText
)

@Composable
fun BaseCenterDialog(
  baseCenterDialogUiModel: BaseCenterDialogUiModel,
  onDismissDialog: () -> Unit = { },
  onClickedConfirm: () -> Unit = { }
) {
  Dialog(
    onDismissRequest = onDismissDialog,
    properties = DialogProperties(
      dismissOnBackPress = true,
      dismissOnClickOutside = true,
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          color = Color.White,
          shape = RoundedCornerShape(10.dp)
        )
        .padding(
          horizontal = 16.dp,
          vertical = 10.dp
        )
    ) {
      Column(
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .padding(
            top = 12.dp,
            bottom = 16.dp
          )
          .verticalScroll(rememberScrollState())
      ) {
        if (baseCenterDialogUiModel.titleMessage.asString().isNotEmpty()) {
          Text(
            modifier = Modifier
              .align(Alignment.CenterHorizontally),
            text = baseCenterDialogUiModel.titleMessage.asString()
          )
        }

        if (baseCenterDialogUiModel.contentMessage.asString().isNotEmpty()) {
          Text(
            modifier = Modifier
              .padding(
                top = 10.dp
              )
              .fillMaxSize()
              .align(Alignment.CenterHorizontally),
            text = baseCenterDialogUiModel.contentMessage.asString(),
            textAlign = TextAlign.Center
          )
        }
      }


      Row(
        modifier = Modifier
          .padding(vertical = 16.dp)
          .fillMaxWidth()
      ) {

        Box(
          modifier = Modifier
              .weight(0.49f)
              .height(48.dp)
              .background(
                  color = Color.DarkGray,
                  shape = RoundedCornerShape(4.dp)
              )
              .clickable(onClick = onClickedConfirm)
        ) {
          Text(
            modifier = Modifier
              .align(Alignment.Center),
            text = baseCenterDialogUiModel.confirmButtonMessage.asString(),
            color = Color.White,
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun BaseCenterDialogPreview() {
  BaseTheme {
    BaseCenterDialog(
      baseCenterDialogUiModel = BaseCenterDialogUiModel(
        titleMessage = UiText.DynamicString("titleMessage"),
        contentMessage = UiText.DynamicString("contentMessage"),
        confirmButtonMessage = UiText.DynamicString("confirmButtonMessage")
      )
    )
  }
}