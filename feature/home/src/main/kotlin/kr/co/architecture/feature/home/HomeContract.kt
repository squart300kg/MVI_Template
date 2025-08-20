package kr.co.architecture.feature.home

import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

data object HomeUiState : UiState

sealed interface HomeUiEvent : UiEvent

sealed interface HomeUiSideEffect : UiSideEffect
