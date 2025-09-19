package kr.co.architecture.feature.detail

import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.model.MediaIdentity
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

enum class DetailUiType {
  NONE,
  LOADED
}

data class UiModel(
  val bindingUiModel: BindingUiModel,
  val unbindingUiModel: UnbindingUiModel
) {
  data class BindingUiModel(
    val title: String,
    val thumbnailUrl: String,
    val isBookmarked: Boolean
  )
  data class UnbindingUiModel(
    val dateTime: String,
    val collection: String?,
    val contents: String,
    val mediaContentsType: MediaContentsTypeEnum
  )
}

fun UiModel.uniqueId(): String =
  MediaIdentity.idOf(
    mediaContentsType = unbindingUiModel.mediaContentsType,
    title = bindingUiModel.title,
    contents = unbindingUiModel.contents,
    thumbnailUrl = bindingUiModel.thumbnailUrl
  )
data class DetailUiState(
  val uiType: DetailUiType = DetailUiType.NONE,
  val uiModel: List<UiModel> = emptyList(),
  val startIndex: Int = 0,
) : UiState

sealed interface DetailUiEvent : UiEvent {
  data class OnClickedBookmark(val uiModel: UiModel): DetailUiEvent
  data object OnClickedBack: DetailUiEvent
  data class OnSwipe(val position: Int): DetailUiEvent
}
sealed interface DetailUiSideEffect : UiSideEffect {
  data object OnFinish: DetailUiSideEffect
}