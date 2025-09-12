package kr.co.architecture.yeo.feature.detail.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kr.co.architecture.yeo.core.ui.util.UiText
import kr.co.architecture.yeo.feature.detail.DetailUiState
import kr.co.architecture.yeo.feature.detail.DetailUiType

class DetailUiStatePreviewParam : PreviewParameterProvider<DetailUiState> {
  override val values: Sequence<DetailUiState> = sequenceOf(
    DetailUiState(
      uiType = DetailUiType.NONE
    ),
    DetailUiState(
      uiType = DetailUiType.LOADED,
      isbn = "123",
      thumbnail = "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F4464321%3Ftimestamp%3D20221107221826",
      title = UiText.DynamicString("코틀린 인 액션"),
      publisher = UiText.DynamicString("출판사"),
      authors = UiText.DynamicString("저자이름"),
      isBookmarked = true,
      price = UiText.DynamicString("50,000원"),
      publishDate = UiText.DynamicString("2025년 10월 9일"),
      contents = UiText.DynamicString("아주 좋은 책입니다. 읽어보세요~"),
    )
  )
}