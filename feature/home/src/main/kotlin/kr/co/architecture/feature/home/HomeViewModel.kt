package kr.co.architecture.feature.home

import kr.co.architecture.core.domain.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {

  override fun createInitialState(): HomeUiState {
    return HomeUiState()
  }

  override fun handleEvent(event: HomeUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { HomeUiSideEffect.Load } }

  fun fetchData() {
    launchSafetyWithLoading {
      val names = getListUseCase()
      setState {
        copy(
          uiType = HomeUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}