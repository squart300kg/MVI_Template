package kr.co.architecture.feature.search

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.R as coreUiR

enum class SearchUiType {
  NONE,
  LOADED
}

data class UiModel(
  val thumbnail: String,
  val title: UiText,
  val publisher: UiText,
  val authors: UiText,
  val isBookmarked: Boolean,
  val price: UiText
) {
  companion object {
    fun mapperToUi(searchedBook: SearchedBook): ImmutableList<UiModel> {
      return searchedBook.books
        .map { book ->
          UiModel(
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
            price = UiText.StringResource(
              resId = coreUiR.string.won,
              args = listOf(book.price.value)
            )
          )
        }
        .toImmutableList()
    }
  }
}

data class SearchUiState(
  val uiType: SearchUiType = SearchUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf(),
  val page: Int = 1,
  val isLoading: Boolean = false
) : UiState

sealed interface SearchUiEvent : UiEvent {
  data class OnClickedItem(val item: UiModel) : SearchUiEvent
}

sealed interface HomeUiSideEffect : UiSideEffect {
  data object Load : HomeUiSideEffect
}
