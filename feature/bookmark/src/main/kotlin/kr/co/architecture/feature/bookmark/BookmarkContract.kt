package kr.co.architecture.feature.bookmark

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.co.architecture.core.ui.BookCardUiModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.enums.SortDirectionUiEnum
import kr.co.architecture.core.ui.enums.SortPriceRangeUiEnum

enum class BookmarkUiType {
  NONE,
  EMPTY_RESULT,
  LOADED_RESULT
}

data class BookmarkUiState(
  val uiType: BookmarkUiType = BookmarkUiType.NONE,
  val sortDirectionUiEnum: SortDirectionUiEnum = SortDirectionUiEnum.ASCENDING,
  val sortPriceRangeUiEnum: SortPriceRangeUiEnum = SortPriceRangeUiEnum.ALL,
  val bookCardUiModels: ImmutableList<BookCardUiModel> = persistentListOf(),
) : UiState

sealed interface BookmarkUiEvent : UiEvent {
  data class OnClickedItem(val isbn: String) : BookmarkUiEvent
  data class OnClickedBookmark(val isbn: String, val isBookmarked: Boolean) : BookmarkUiEvent
  data class OnQueryChange(val query: String) : BookmarkUiEvent
  data class OnChangeSortDirection(val uiEnum: SortDirectionUiEnum) : BookmarkUiEvent
  data class OnChangePriceRange(val uiEnum: SortPriceRangeUiEnum) : BookmarkUiEvent
}

sealed interface BookmarkUiSideEffect : UiSideEffect