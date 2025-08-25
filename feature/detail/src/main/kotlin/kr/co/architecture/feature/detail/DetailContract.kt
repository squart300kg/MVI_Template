package kr.co.architecture.feature.detail

import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText

enum class DetailUiType {
  NONE,
  LOADED
}

data class UiModel(
  val name: UiText
)

data class DetailUiState(
  val uiType: DetailUiType = DetailUiType.NONE,
  val uiModel: UiModel? = null
) : UiState

sealed interface DetailUiEvent : UiEvent {

}

sealed interface DetailUiSideEffect : UiSideEffect {
  data object Load : DetailUiSideEffect
}