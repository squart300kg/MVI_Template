package kr.co.architecture.yeo.feature.detail

import kr.co.architecture.yeo.core.ui.util.formatter.DateTextFormatter
import kr.co.architecture.yeo.core.ui.util.formatter.MoneyTextFormatter
import kr.co.architecture.yeo.core.domain.entity.Book
import kr.co.architecture.yeo.core.domain.entity.Price
import kr.co.architecture.yeo.core.ui.UiEvent
import kr.co.architecture.yeo.core.ui.UiSideEffect
import kr.co.architecture.yeo.core.ui.UiState
import kr.co.architecture.yeo.core.ui.util.UiText
import kr.co.architecture.yeo.core.ui.R as coreUiR

enum class DetailUiType {
  NONE,
  LOADED
}

data class DetailUiState(
  val uiType: DetailUiType = DetailUiType.NONE,
  val isbn: String = "",
  val thumbnail: String = "",
  val title: UiText = UiText.DynamicString(""),
  val publisher: UiText = UiText.DynamicString(""),
  val authors: UiText = UiText.DynamicString(""),
  val isBookmarked: Boolean = false,
  val price: UiText = UiText.DynamicString(""),
  val publishDate: UiText = UiText.DynamicString(""),
  val contents: UiText = UiText.DynamicString(""),
) : UiState {
  companion object {
    fun mapperToUi(
      book: Book,
      dateTextFormatter: DateTextFormatter,
      moneyTextFormatter: MoneyTextFormatter
    ) = DetailUiState(
      uiType = DetailUiType.LOADED,
      isbn = book.isbn,
      thumbnail = book.thumbnail,
      title = UiText.DynamicString(book.title),
      publisher = UiText.StringResource(
        resId = coreUiR.string.publisher,
        args = listOf(book.publisher)
      ),
      authors = UiText.StringResource(
        resId = coreUiR.string.authors,
        args = listOf(book.authors.joinToString(", "))
      ),
      isBookmarked = book.isBookmarked,
      price = run {
        val displayedPrice = when (val price = book.price) {
          is Price.Discount -> price.discounted
          is Price.Origin -> price.origin
        }
        UiText.StringResource(
          resId = coreUiR.string.won,
          args = listOf(moneyTextFormatter(displayedPrice))
        )
      },
      publishDate = UiText.StringResource(
        resId = coreUiR.string.publishDate,
        args = listOf(dateTextFormatter(book.dateTime))
      ),
      contents = UiText.DynamicString(book.contents)
    )
  }
}


sealed interface DetailUiEvent : UiEvent {
  data object OnClickedBookmark: DetailUiEvent
  data object OnClickedBack: DetailUiEvent
}

sealed interface DetailUiSideEffect : UiSideEffect