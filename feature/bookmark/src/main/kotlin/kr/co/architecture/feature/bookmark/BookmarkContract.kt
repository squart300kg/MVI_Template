package kr.co.architecture.feature.bookmark

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.domain.formatter.EraseDateUnderDayFormatter
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

enum class BookmarkUiType {
  NONE,
  LOADED_RESULT,
  EMPTY_RESULT
}

// TODO: uiModel의 text들 UiText로 변경
data class UiModel(
  val title: String,
  val thumbnailUrl: String,
  val mediaContentsType: MediaContentsTypeEnum,
  val dateTime: String,
  val isBookmarked: Boolean = true
) {
  companion object {
    fun mapperToUiModel(
      contents: Set<MediaContents>,
      eraseDateUnderDayFormatter: EraseDateUnderDayFormatter
    ) =
      contents.map {
        UiModel(
          title = it.title,
          thumbnailUrl = it.thumbnailUrl,
          mediaContentsType = it.mediaContentsType,
          dateTime = eraseDateUnderDayFormatter(it.dateTime)
        )
      }.toImmutableList()

  }
}

data class BookmarkUiState(
  val uiType: BookmarkUiType = BookmarkUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf()
) : UiState

sealed interface BookmarkUiEvent : UiEvent {
  data class OnClickedItem(val uiModel: UiModel) : BookmarkUiEvent
  data class OnClickedBookmark(val uiModel: UiModel) : BookmarkUiEvent
}

sealed interface BookmarkUiSideEffect : UiSideEffect {
  data class OnStartDetailActivity(val hellO: String): BookmarkUiSideEffect
}