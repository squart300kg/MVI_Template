package kr.co.architecture.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.co.architecture.core.ui.preview.BaseDialogUiModelPreviewParam
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.asString

data class BaseCenterDialogUiModel(
  val titleMessage: UiText,
  val contentMessage: UiText,
  val confirmButtonMessage: UiText = UiText.StringResource(R.string.confirm)
)

@Composable
fun BaseCenterDialog(
  baseCenterDialogUiModel: BaseCenterDialogUiModel,
  onDismissDialog: () -> Unit = {},
  onClickedConfirm: () -> Unit = {}
) {
  Dialog(
    onDismissRequest = onDismissDialog,
    properties = DialogProperties(
      dismissOnBackPress = true,
      dismissOnClickOutside = true
    )
  ) {
    // 화면 높이의 90%를 상한으로만 설정 (작으면 wrap-content)
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val maxDialogHeight = with(density) { windowInfo.containerSize.height.toDp() * 0.9f }

    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = maxDialogHeight),
      shape = RoundedCornerShape(10.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Column(
          modifier = Modifier
            .weight(1f, fill = false)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          val title = baseCenterDialogUiModel.titleMessage.asString()
          if (title.isNotEmpty()) {
            Text(text = title, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
          }

          val content = baseCenterDialogUiModel.contentMessage.asString()
          if (content.isNotEmpty()) {
            Text(
              modifier = Modifier.padding(top = 10.dp),
              text = content,
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
              .weight(1f)
              .height(48.dp)
              .background(
                color = Color.DarkGray,
                shape = RoundedCornerShape(4.dp)
              )
              .clickable(onClick = onClickedConfirm)
          ) {
            Text(
              modifier = Modifier.align(Alignment.Center),
              text = baseCenterDialogUiModel.confirmButtonMessage.asString(),
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun BaseCenterDialogPreview(
  @PreviewParameter(BaseDialogUiModelPreviewParam::class)
  uiModel: BaseCenterDialogUiModel
) {
  BaseTheme {
    BaseCenterDialog(
      baseCenterDialogUiModel = uiModel
    )
  }
}