package kr.co.architecture.feature.search

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.domain.GetSortedImagesAndVideosByRecentlyUseCase
import kr.co.architecture.core.model.ContentsType
import kr.co.architecture.core.repository.dto.PageableDto
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

enum class SearchUiType {
  NONE,
  LOADED_RESULT,
  EMPTY_RESULT
}

data class UiModel(
  val thumbnailUrl: String,
  val dateTime: String,
  val title: String,
  val collection: String? = null,
  val contents: String,
  val contentsType: ContentsType
) {
  companion object {
    fun mapperToUiModel(contents: List<GetSortedImagesAndVideosByRecentlyUseCase.Response.Contents>) =
      contents.map {
        UiModel(
          thumbnailUrl = it.thumbnailUrl,
          dateTime = it.dateTime,
          title = it.title,
          collection = it.collection,
          contents = it.contents,
          contentsType = when (it.collection != null) {
            true -> ContentsType.IMAGE
            false -> ContentsType.VIDEO
          }
        )
      }.toImmutableList()
  }
}

data class SearchUiState(
  val uiType: SearchUiType = SearchUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf(),
  val page: Int = 1,
  val isEndPage: Boolean = true
) : UiState

sealed interface SearchUiEvent : UiEvent {
  data class OnClickedItem(val item: UiModel) : SearchUiEvent
  data object OnScrolledToEnd : SearchUiEvent
}

sealed interface SearchUiSideEffect : UiSideEffect {
  sealed interface Load : SearchUiSideEffect {
    data object First: Load
    data object More: Load
  }
}
