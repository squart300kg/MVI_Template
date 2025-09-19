package kr.co.architecture.feature.detail

import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

enum class DetailUiType {
  NONE,
  LOADED
}

data class DetailUiState(
  val uiType: DetailUiType = DetailUiType.NONE,
  val mediaContents: List<MediaContents> = emptyList(),
  val startIndex: Int = 0,
) : UiState

sealed interface DetailUiEvent : UiEvent {
  data class OnClickedBookmark(val mediaContents: MediaContents): DetailUiEvent
  data object OnClickedBack: DetailUiEvent
  data class OnSwipe(val position: Int): DetailUiEvent
}
sealed interface DetailUiSideEffect : UiSideEffect {
  data object OnFinish: DetailUiSideEffect
}