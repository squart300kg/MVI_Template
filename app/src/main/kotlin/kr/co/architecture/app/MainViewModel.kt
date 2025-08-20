package kr.co.architecture.app

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.app.MainUiState
import kr.co.architecture.core.repository.Repository
import kr.co.architecture.core.ui.BaseCenterDialogUiModel
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import javax.inject.Inject

data class MainUiState(
  val errorDialog: BaseCenterDialogUiModel? = null,
  val isLoading: Boolean = false,
  val isRefresh: Boolean = false,
): UiState
sealed interface MainUiEvent : UiEvent {
  data object OnClickedErrorDialogConfirm : MainUiEvent
}
sealed interface MainUiSideEffect : UiSideEffect

@HiltViewModel
class MainViewModel @Inject constructor(

) : BaseViewModel<MainUiState, MainUiEvent, MainUiSideEffect>() {

  override fun createInitialState(): MainUiState {
    return MainUiState()
  }

  override fun handleEvent(event: MainUiEvent) {
    when (event) {
      is MainUiEvent.OnClickedErrorDialogConfirm -> {
        setState { copy(errorDialog = null) }
      }
    }
  }

  fun showErrorDialog(uiModel: BaseCenterDialogUiModel) {
    setState { copy(errorDialog = uiModel) }
  }

  fun setLoadingState(isLoading: Boolean) {
    setState { copy(isLoading = isLoading) }
  }

  fun setRefreshState(isRefresh: Boolean) {
    setState { copy(isRefresh = isRefresh) }
  }
}