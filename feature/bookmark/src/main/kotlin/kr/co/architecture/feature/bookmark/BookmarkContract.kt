package kr.co.architecture.feature.bookmark

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.co.architecture.core.ui.BookUiModel
import kr.co.architecture.core.ui.SearchHeaderUiModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.enums.SortTypeUiEnum

enum class BookmarkUiType {
  NONE,
  LOADED
}

data class BookmarkUiState(
  val uiType: BookmarkUiType = BookmarkUiType.NONE,
  val searchHeaderUiModel: SearchHeaderUiModel = SearchHeaderUiModel(),
  val bookUiModels: ImmutableList<BookUiModel> = persistentListOf(),
) : UiState

sealed interface BookmarkUiEvent : UiEvent {
  data class OnClickedItem(val isbn: String) : BookmarkUiEvent
  data class OnClickedBookmark(val isbn: String, val isBookmarked: Boolean) : BookmarkUiEvent
  data class OnQueryChange(val query: String) : BookmarkUiEvent
  data object OnSearch : BookmarkUiEvent
}

sealed interface BookmarkUiSideEffect : UiSideEffect {
  data object Load : BookmarkUiSideEffect
}