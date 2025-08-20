package kr.co.architecture.core.ui

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.co.architecture.core.ui.CenterDialogUiModel.ButtonStyleVo
import kr.co.architecture.core.ui.preview.BaseCenterDialogVoPreviewParam
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.core.ui.theme.TypoBody1
import kr.co.architecture.core.ui.theme.TypoSubhead3
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.asString

data class CenterDialogUiModel(
  val titleMessage: UiText = UiText.DynamicString(""),
  val contentMessage: UiText = UiText.DynamicString(""),
  val checkBoxVoState: CheckBoxVo? = null,
  val buttonStyleVo: ButtonStyleVo
) {
  data class CheckBoxVo(
    val checkState: Boolean,
    val onCheckStateChanged: (Boolean) -> Unit = {},
    val checkMessage: UiText
  )
  sealed interface ButtonStyleVo {
    data class Type1(
      override val leftMessage: UiText,
      override val rightMessage: UiText
    ) : ButtonStyleVo, TwoButtonStyle {
      @ColorRes override val leftTextColor: Int = R.color.gray_091E42
      @ColorRes override val leftBackgroundColor: Int = R.color.gray_C8CDD5
      @ColorRes override val leftBorderColor: Int = R.color.gray_C8CDD5
      @ColorRes override val rightTextColor: Int = R.color.white
      @ColorRes override val rightBackgroundColor: Int = R.color.red
      @ColorRes override val rightBorderColor: Int = R.color.red
    }
    data class Type2(
      override val leftMessage: UiText,
      override val rightMessage: UiText
    ) : ButtonStyleVo, TwoButtonStyle {
      @ColorRes override val leftTextColor: Int = R.color.gray_091E42
      @ColorRes override val leftBackgroundColor: Int = R.color.gray_C8CDD5
      @ColorRes override val leftBorderColor: Int = R.color.gray_C8CDD5
      @ColorRes override val rightTextColor: Int = R.color.white
      @ColorRes override val rightBackgroundColor: Int = R.color.gray_091E42
      @ColorRes override val rightBorderColor: Int = R.color.gray_091E42
    }
    data class Type3(
      override val leftMessage: UiText,
      override val rightMessage: UiText
    ) : ButtonStyleVo, TwoButtonStyle {
      @ColorRes override val leftTextColor: Int = R.color.gray_091E42
      @ColorRes override val leftBackgroundColor: Int = R.color.white
      @ColorRes override val leftBorderColor: Int = R.color.gray_091E42
      @ColorRes override val rightTextColor: Int = R.color.white
      @ColorRes override val rightBackgroundColor: Int = R.color.gray_091E42
      @ColorRes override val rightBorderColor: Int = R.color.gray_091E42
    }
    data class Type4(
      override val message: UiText
    ) : ButtonStyleVo, OneButtonStyle {
      @ColorRes override val textColor: Int = R.color.white
      @ColorRes override val backgroundColor: Int = R.color.gray_091E42
      @ColorRes override val borderColor: Int = R.color.gray_091E42
    }
  }
}

@Composable
fun BaseCenterDialog(
  uiModel: CenterDialogUiModel,
  onClickedLeftButton: () -> Unit = { },
  onClickedRightButton: () -> Unit = { },
  onClickedCenterButton: () -> Unit = { },
  onLongClickedErrorContent: () -> Unit = { },
  onDismissDialog: () -> Unit = { },
) {
  Dialog(
    onDismissRequest = onDismissDialog,
    properties = DialogProperties(
      dismissOnBackPress = true,
      usePlatformDefaultWidth = false,
      dismissOnClickOutside = true,
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .background(
          color = Color.White,
          shape = RoundedCornerShape(10.dp)
        )
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .padding(
            top = 12.dp,
            bottom = 16.dp
          )
      ) {
        Text(
          modifier = Modifier
            .align(Alignment.CenterHorizontally),
          text = uiModel.titleMessage.asString(),
          style = TypoSubhead3.copy(
            color = colorResource(id = R.color.gray_091E42),
            fontWeight = FontWeight.W700
          )
        )

        Box(
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .heightIn(max = 300.dp)
            .verticalScroll(rememberScrollState())
        ) {
          Text(
            modifier = Modifier
              .padding(top = 10.dp),
            text = uiModel.contentMessage.asString(),
            style = TypoBody1.copy(
              color = colorResource(id = R.color.gray_7A869A),
              fontWeight = FontWeight.W400
            ),
            textAlign = TextAlign.Center
          )
        }
      }

      uiModel.checkBoxVoState?.let { checkBoxState ->
        Row(
          modifier = Modifier
            .padding(
              top = 16.dp,
              bottom = 24.dp,
            )
            .fillMaxWidth()
            .border(
              width = 1.dp,
              color = colorResource(id = R.color.gray_DFE1E6),
              shape = RoundedCornerShape(4.dp)
            )
            .padding(
              vertical = 14.dp,
              horizontal = 10.dp
            )
            .noRippledClickable(
              onClick = { checkBoxState.onCheckStateChanged(!checkBoxState.checkState) }
            )
        ) {
          Checkbox(
            modifier = Modifier
              .padding(start = 4.dp)
              .size(12.dp)
              .align(Alignment.CenterVertically),
            checked = checkBoxState.checkState,
            colors = CheckboxColors(
              checkedCheckmarkColor = colorResource(id = R.color.gray_091E42),
              uncheckedCheckmarkColor = Color.Transparent,
              checkedBoxColor = Color.Transparent,
              uncheckedBoxColor = Color.Transparent,
              disabledCheckedBoxColor = Color.Transparent,
              disabledUncheckedBoxColor = Color.Transparent,
              disabledIndeterminateBoxColor = Color.Transparent,
              checkedBorderColor = colorResource(id = R.color.gray_091E42),
              uncheckedBorderColor = colorResource(id = R.color.gray_B3BAC5),
              disabledBorderColor = Color.Transparent,
              disabledUncheckedBorderColor = Color.Transparent,
              disabledIndeterminateBorderColor = Color.Transparent,
            ),
            onCheckedChange = { checkBoxState.onCheckStateChanged(it) }
          )

          Text(
            modifier = Modifier
              .padding(start = 14.dp),
            text = stringResource(id = R.string.buddy_require_block),
            style = TypoBody1.copy(
              color = colorResource(id = R.color.gray_091E42),
              fontWeight = FontWeight.W400
            )
          )
        }
      }

      when (uiModel.buttonStyleVo) {
        is ButtonStyleVo.Type1,
        is ButtonStyleVo.Type2,
        is ButtonStyleVo.Type3 -> {
          // 타입체크 못해서 걸어둠
          if (uiModel.buttonStyleVo is TwoButtonStyle) {
            Row {
              BaseDialogButton(
                modifier = Modifier.weight(0.49f),
                message = uiModel.buttonStyleVo.leftMessage.asString(),
                textColor = uiModel.buttonStyleVo.leftTextColor,
                backgroundColor = uiModel.buttonStyleVo.leftBackgroundColor,
                borderColor = uiModel.buttonStyleVo.leftBorderColor,
                onClickedConfirm = onClickedLeftButton
              )

              Spacer(modifier = Modifier.weight(0.02f))

              BaseDialogButton(
                modifier = Modifier.weight(0.49f),
                message = uiModel.buttonStyleVo.rightMessage.asString(),
                textColor = uiModel.buttonStyleVo.rightTextColor,
                backgroundColor = uiModel.buttonStyleVo.rightBackgroundColor,
                borderColor = uiModel.buttonStyleVo.rightBorderColor,
                onClickedConfirm = onClickedRightButton
              )
            }
          }
        }
        is ButtonStyleVo.Type4 -> {
          // 타입체크 못해서 걸어둠
          if (uiModel.buttonStyleVo is OneButtonStyle) {
            BaseDialogButton(
              modifier = Modifier
                .noRippledClickable(
                  onLongClick = onLongClickedErrorContent
                ),
              message = uiModel.buttonStyleVo.message.asString(),
              textColor = uiModel.buttonStyleVo.textColor,
              backgroundColor = uiModel.buttonStyleVo.backgroundColor,
              borderColor = uiModel.buttonStyleVo.borderColor,
              onClickedConfirm = { onClickedCenterButton() }
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun BaseCenterDialog2Preview(
  @PreviewParameter(BaseCenterDialogVoPreviewParam::class)
  centerDialogUiModelPreviewParam: CenterDialogUiModel
) {
  BaseTheme {
    BaseCenterDialog(
      uiModel = centerDialogUiModelPreviewParam,
      onDismissDialog = { }
    )
  }
}