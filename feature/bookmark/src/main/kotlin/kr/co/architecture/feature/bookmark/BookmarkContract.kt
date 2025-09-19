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

data class UiModel(
  val bindingUiModel: BindingUiModel,
  val unbindingUiModel: UnbindingUiModel
) {
  companion object {
    fun mapperToUiModel(
      contents: Set<MediaContents>,
      eraseDateUnderDayFormatter: EraseDateUnderDayFormatter
    ) = contents.map {
      UiModel(
        bindingUiModel = BindingUiModel.mapperToUiModel(
          contents = it,
          eraseDateUnderDayFormatter = eraseDateUnderDayFormatter
        ),
        unbindingUiModel = UnbindingUiModel(
          contents = it.contents
        )
      )
    }.toImmutableList()

    fun mapperToDomainModel(uiModelsState: UiModel) =
      MediaContents(
        thumbnailUrl = uiModelsState.bindingUiModel.thumbnailUrl,
        dateTime = uiModelsState.bindingUiModel.dateTime,
        title = uiModelsState.bindingUiModel.title,
        contents = uiModelsState.unbindingUiModel.contents,
        mediaContentsType = uiModelsState.bindingUiModel.mediaContentsType,
      )
  }
}

// TODO: uiModel의 text들 UiText로 변경
data class BindingUiModel(
  val title: String,
  val thumbnailUrl: String,
  val mediaContentsType: MediaContentsTypeEnum,
  val dateTime: String,
  val isBookmarked: Boolean = true
) {
  companion object {
    fun mapperToUiModel(
      contents: MediaContents,
      eraseDateUnderDayFormatter: EraseDateUnderDayFormatter
    ) = BindingUiModel(
      title = contents.title,
      thumbnailUrl = contents.thumbnailUrl,
      mediaContentsType = contents.mediaContentsType,
      dateTime = eraseDateUnderDayFormatter(contents.dateTime)
    )
  }

}

data class UnbindingUiModel(
  val contents: String
)

data class BookmarkUiState(
  val uiType: BookmarkUiType = BookmarkUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf()
) : UiState

sealed interface BookmarkUiEvent : UiEvent {
  data class OnClickedItem(val uiModelState: UiModel) : BookmarkUiEvent
  data class OnClickedBookmark(val uiModelState: UiModel) : BookmarkUiEvent
}

sealed interface BookmarkUiSideEffect : UiSideEffect {
  data class OnStartDetailActivity(val hellO: String): BookmarkUiSideEffect
}