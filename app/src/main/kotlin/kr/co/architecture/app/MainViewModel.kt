package kr.co.architecture.app

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.app.ui.navigation.MainBottomTab
import kr.co.architecture.core.ui.BaseCenterDialogUiModel
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.BookmarkRoute
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import javax.inject.Inject

data class MainUiState(
  val errorDialog: BaseCenterDialogUiModel? = null,
  val isLoading: Boolean = false
): UiState
sealed interface MainUiEvent : UiEvent {
  data class OnClickedBottomTab(val tab: MainBottomTab) : MainUiEvent
  data object OnClickedErrorDialogConfirm : MainUiEvent
}
sealed interface MainUiSideEffect : UiSideEffect

@HiltViewModel
class MainViewModel @Inject constructor(

) : BaseViewModel<MainUiState, MainUiEvent, MainUiSideEffect>() {

  override fun createInitialState() = MainUiState()

  override fun handleEvent(event: MainUiEvent) {
    when (event) {
      is MainUiEvent.OnClickedBottomTab -> {
        when (event.tab.route) {
          is SearchRoute -> navigateTo(
            route = SearchRoute,
            saveState = true,
            launchSingleTop = true
          )
          is BookmarkRoute -> navigateTo(
            route = BookmarkRoute,
            saveState = true,
            launchSingleTop = true
          )
        }
      }
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
}