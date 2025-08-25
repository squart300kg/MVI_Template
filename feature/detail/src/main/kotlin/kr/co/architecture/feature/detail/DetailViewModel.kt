package kr.co.architecture.feature.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : BaseViewModel<DetailUiState, DetailUiEvent, DetailUiSideEffect>() {

  override fun createInitialState(): DetailUiState {
    return DetailUiState()
  }

  override fun handleEvent(event: DetailUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { DetailUiSideEffect.Load } }

  fun fetchData() {
    launchSafetyWithLoading {

    }
  }
}