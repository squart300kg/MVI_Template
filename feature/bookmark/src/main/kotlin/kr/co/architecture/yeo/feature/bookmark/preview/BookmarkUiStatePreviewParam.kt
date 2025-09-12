package kr.co.architecture.yeo.feature.bookmark.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.yeo.core.ui.BookCardUiModel
import kr.co.architecture.yeo.core.ui.util.UiText
import kr.co.architecture.yeo.feature.bookmark.BookmarkUiState
import kr.co.architecture.yeo.feature.bookmark.BookmarkUiType

class BookmarkUiStatePreviewParam : PreviewParameterProvider<BookmarkUiState> {
  override val values: Sequence<BookmarkUiState> = sequenceOf(
    BookmarkUiState(
      uiType = BookmarkUiType.NONE
    ),
    BookmarkUiState(
      uiType = BookmarkUiType.EMPTY_RESULT
    ),
    BookmarkUiState(
      uiType = BookmarkUiType.LOADED_RESULT,
      bookCardUiModels = List(10) {
        BookCardUiModel(
          isbn = "$it",
          thumbnail = "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F4464321%3Ftimestamp%3D20221107221826",
          title = UiText.DynamicString("코틀린 인액션_$it"),
          publisher = UiText.DynamicString("출판사_$it"),
          authors = UiText.DynamicString("저자_$it"),
          isBookmarked = true,
          price = UiText.DynamicString("50,000원"),
          publishDate = UiText.DynamicString("2020년 10월 ${it}일"),
        )
      }.toImmutableList()
    )
  )
}