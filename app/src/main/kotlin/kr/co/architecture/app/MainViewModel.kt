package kr.co.architecture.app

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import javax.inject.Inject

data object MainUiState: UiState
sealed interface MainUiEvent : UiEvent {
  data object OnClickedErrorDialogConfirm : MainUiEvent
}
sealed interface MainUiSideEffect : UiSideEffect

@HiltViewModel
class MainViewModel @Inject constructor(

) : BaseViewModel<MainUiState, MainUiEvent, MainUiSideEffect>() {

  override fun createInitialState() = MainUiState

  override fun handleEvent(event: MainUiEvent) {
    when (event) {
      is MainUiEvent.OnClickedErrorDialogConfirm -> {
        globalUiBus.dismissDialog()
      }
    }
  }
}