package kr.co.architecture.feature.search

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.common.formatter.DateTextFormatter
import kr.co.architecture.core.common.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.enums.SortTypeUiEnum
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.R as coreUiR

enum class SearchUiType {
  NONE,
  LOADED
}

data class BookUiModel(
  val isbn: String,
  val thumbnail: String,
  val title: UiText,
  val publisher: UiText,
  val authors: UiText,
  val isBookmarked: Boolean,
  val price: UiText,
  val publishDate: UiText,
) {

  companion object {
    fun mapperToUi(
      searchedBooks: SearchedBooks,
      dateTextFormatter: DateTextFormatter,
      moneyTextFormatter: MoneyTextFormatter
    ): ImmutableList<BookUiModel> {
      return searchedBooks.books
        .map { book ->
          BookUiModel(
            isbn = book.isbn,
            thumbnail = book.thumbnail,
            title = UiText.DynamicString(book.title),
            publisher = UiText.StringResource(
              resId = coreUiR.string.publisher,
              args = listOf(book.publisher)
            ),
            authors = UiText.StringResource(
              resId = coreUiR.string.authors,
              args = listOf(
                book.authors
                  .joinToString(", ")
              )
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
            )
          )
        }
        .toImmutableList()
    }
  }
}

data class SearchUiState(
  val uiType: SearchUiType = SearchUiType.NONE,
  val bookUiModels: ImmutableList<BookUiModel> = persistentListOf(),
  val page: Int = 1,
  val query: String = "ㄷ",
  val sort: SortTypeUiEnum = SortTypeUiEnum.ACCURACY,
  val isPageable: Boolean = true,
  val isLoading: Boolean = false
) : UiState

sealed interface SearchUiEvent : UiEvent {
  data class OnClickedItem(val item: BookUiModel) : SearchUiEvent
  data class OnClickedBookmark(val item: BookUiModel) : SearchUiEvent
  data object OnScrolledToEnd : SearchUiEvent
  data class OnQueryChange(val query: String) : SearchUiEvent
  data object OnSearch : SearchUiEvent
  data class OnChangeSort(val sort: SortTypeUiEnum) : SearchUiEvent
}

sealed interface SearchUiSideEffect : UiSideEffect {
  sealed interface Load: SearchUiSideEffect {
    data object First: Load
    data object More: Load
  }
}
