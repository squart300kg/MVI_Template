package kr.co.architecture.feature.second

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.GetListUseCase
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<SecondUiState, SecondUiEvent, SecondUiSideEffect>() {

  override fun createInitialState() = SecondUiState()

  override fun handleEvent(event: SecondUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { SecondUiSideEffect.Load } }

  fun fetchData() {
    launchWithCatching {
      val names = getListUseCase()
      setState {
        copy(
          uiType = SecondUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}
