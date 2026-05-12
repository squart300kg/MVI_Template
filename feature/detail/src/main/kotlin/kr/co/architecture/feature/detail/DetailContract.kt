package kr.co.architecture.feature.detail

import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

enum class DetailUiType {
  NONE,
  LOADED
}

data class DetailUiState(
  val uiType: DetailUiType = DetailUiType.NONE,
  val id: String = "",
  val name: String = ""
) : UiState

sealed interface DetailUiEvent : UiEvent {

}

sealed interface DetailUiSideEffect : UiSideEffect {
  data object Load : DetailUiSideEffect
}
