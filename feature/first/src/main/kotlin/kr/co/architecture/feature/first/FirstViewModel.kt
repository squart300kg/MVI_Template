package kr.co.architecture.feature.first

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.GetListUseCase
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<FirstUiState, FirstUiEvent, FirstUiSideEffect>() {

  override fun createInitialState() = FirstUiState()

  override fun handleEvent(event: FirstUiEvent) {}

  init { setEffect { FirstUiSideEffect.Load } }

  fun fetchData() {
    launchWithLoading {
      val names = getListUseCase()
      setState {
        copy(
          uiType = FirstUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}