package kr.co.architecture.feature.search.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.BookUiModel
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.feature.search.SearchUiState
import kr.co.architecture.feature.search.SearchUiType

class SearchUiStatePreviewParam : PreviewParameterProvider<SearchUiState> {
  override val values: Sequence<SearchUiState> = sequenceOf(
    SearchUiState(
      uiType = SearchUiType.NONE
    ),
    SearchUiState(
      uiType = SearchUiType.EMPTY_RESULT
    ),
    SearchUiState(
      uiType = SearchUiType.LOADED_RESULT,
      query = UiText.DynamicString("코틀린 인액션"),
      bookUiModels = List(10) {
        BookUiModel(
          isbn = "$it",
          thumbnail = "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F4464321%3Ftimestamp%3D20221107221826",
          title = UiText.DynamicString("코틀린 인액션_$it"),
          publisher = UiText.DynamicString("출판사_$it"),
          authors = UiText.DynamicString("저자_$it"),
          isBookmarked = it % 2 == 0,
          price = UiText.DynamicString("50,000원"),
          publishDate = UiText.DynamicString("2020년 10월 ${it}일"),
        )
      }.toImmutableList()
    )
  )
}