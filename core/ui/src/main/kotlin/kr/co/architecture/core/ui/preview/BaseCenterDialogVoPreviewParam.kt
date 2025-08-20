package kr.co.architecture.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kr.co.architecture.core.ui.CenterDialogUiModel
import kr.co.architecture.core.ui.CenterDialogUiModel.ButtonStyleVo
import kr.co.architecture.core.ui.util.UiText

class BaseCenterDialogVoPreviewParam : PreviewParameterProvider<CenterDialogUiModel> {
  override val values: Sequence<CenterDialogUiModel> = sequenceOf(
    CenterDialogUiModel(
      titleMessage = UiText.DynamicString("버디를 삭제할까요?"),
      contentMessage = UiText.DynamicString("버디를 삭제하면 버디의 주식 정보를 열람할 수 없어요."),
      checkBoxVoState = CenterDialogUiModel.CheckBoxVo(
        checkState = true,
        checkMessage = UiText.DynamicString("앞으로 친구의 버디요청도 차단할게요")
      ),
      buttonStyleVo = ButtonStyleVo.Type1(
        leftMessage = UiText.DynamicString("취소"),
        rightMessage = UiText.DynamicString(
          "삭제)"
        ),
      )
    ),
    CenterDialogUiModel(
      titleMessage = UiText.DynamicString("버디를 삭제할까요?"),
      contentMessage = UiText.DynamicString("버디를 삭제하면 버디의 주식 정보를 열람할 수 없어요."),
      checkBoxVoState = CenterDialogUiModel.CheckBoxVo(
        checkState = false,
        checkMessage = UiText.DynamicString("앞으로 친구의 버디요청도 차단할게요")
      ),
      buttonStyleVo = ButtonStyleVo.Type2(
        leftMessage = UiText.DynamicString("취소"),
        rightMessage = UiText.DynamicString("삭제")
      ),
    ),
    CenterDialogUiModel(
      titleMessage = UiText.DynamicString("버디를 삭제할까요?"),
      contentMessage = UiText.DynamicString("버디를 삭제하면 버디의 주식 정보를 열람할 수 없어요."),
      buttonStyleVo = ButtonStyleVo.Type3(
        leftMessage = UiText.DynamicString("취소"),
        rightMessage = UiText.DynamicString("삭제")
      ),
    ),
    CenterDialogUiModel(
      titleMessage = UiText.DynamicString("버디를 삭제할까요?"),
      contentMessage = UiText.DynamicString("버디를 삭제하면 버디의 주식 정보를 열람할 수 없어요."),
      buttonStyleVo = ButtonStyleVo.Type4(
        message = UiText.DynamicString("확인")
      ),
    ),
    CenterDialogUiModel(
      titleMessage = UiText.DynamicString("[500]"),
      contentMessage = UiText.DynamicString(
        "ErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTraceErrorStackTrace"
      ),
      buttonStyleVo = ButtonStyleVo.Type4(
        message = UiText.DynamicString("확인")
      ),
    ),
  )
}