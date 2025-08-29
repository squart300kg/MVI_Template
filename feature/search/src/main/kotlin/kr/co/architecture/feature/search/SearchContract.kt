package kr.co.architecture.feature.search

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.co.architecture.core.ui.BookUiModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.enums.SortUiEnum

enum class SearchUiType {
  NONE,
  EMPTY_RESULT,
  LOADED_RESULT
}

data class SearchUiState(
  val uiType: SearchUiType = SearchUiType.NONE,
  val sort: SortUiEnum = SortUiEnum.ACCURACY,
  val bookUiModels: ImmutableList<BookUiModel> = persistentListOf(),
  val page: Int = 1,
  val isPageable: Boolean = true,
  val isLoading: Boolean = false
) : UiState

sealed interface SearchUiEvent : UiEvent {
  data class OnClickedItem(val isbn: String) : SearchUiEvent
  data class OnClickedBookmark(val isbn: String, val isBookmarked: Boolean) : SearchUiEvent
  data object OnScrolledToEnd : SearchUiEvent
  data class OnQueryChange(val query: String) : SearchUiEvent
  data object OnSearch : SearchUiEvent
  data class OnChangeSort(val sort: SortUiEnum) : SearchUiEvent
}

sealed interface SearchUiSideEffect : UiSideEffect {
  sealed interface Load: SearchUiSideEffect {
    data object First: Load
    data object More: Load
  }
}
