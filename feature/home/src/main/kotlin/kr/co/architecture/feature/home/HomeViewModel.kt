package kr.co.architecture.feature.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {

  override fun createInitialState(): HomeUiState {
    return HomeUiState
  }

  override fun handleEvent(event: HomeUiEvent) { }

}