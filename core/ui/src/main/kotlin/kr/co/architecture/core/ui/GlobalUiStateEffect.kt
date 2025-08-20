package kr.co.architecture.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.launch

@Composable
fun <UI_STATE : UiState,
  UI_EVENT : UiEvent,
  UI_SIDE_EFFECT : UiSideEffect,
  VIEWMODEL : BaseViewModel<UI_STATE, UI_EVENT, UI_SIDE_EFFECT>>
  GlobalUiStateEffect(viewModel: VIEWMODEL) {
  val localOnErrorMessageChanged by rememberUpdatedState(LocalOnErrorMessageChanged.current)
  val localOnLoadingStateChanged by rememberUpdatedState(LocalOnLoadingStateChanged.current)
  val localOnRefreshStateChanged by rememberUpdatedState(LocalOnRefreshStateChanged.current)
  LaunchedEffect(Unit) {
    launch {
      viewModel.errorMessageState.collect {
        localOnErrorMessageChanged(it)
      }
    }
    launch {
      viewModel.loadingState.collect {
        localOnLoadingStateChanged(it)
      }
    }
    launch {
      viewModel.refreshState.collect {
        localOnRefreshStateChanged(it)
      }
    }
  }
}