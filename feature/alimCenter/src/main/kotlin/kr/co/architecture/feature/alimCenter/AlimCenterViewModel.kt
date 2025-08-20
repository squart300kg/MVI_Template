package kr.co.architecture.feature.alimCenter

import kr.co.architecture.core.domain.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AlimCenterViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<AlimCenterUiState, AlimCenterUiEvent, AlimCenterUiSideEffect>() {

  override fun createInitialState(): AlimCenterUiState {
    return AlimCenterUiState()
  }

  override fun handleEvent(event: AlimCenterUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { AlimCenterUiSideEffect.Load } }

  fun fetchData() {
    launchSafetyWithLoading {
      val names = getListUseCase()
      setState {
        copy(
          uiType = AlimCenterUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}