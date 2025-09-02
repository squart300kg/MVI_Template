package kr.co.architecture.feature.first

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.GetListUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.DetailRoute
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<FirstUiState, FirstUiEvent, FirstUiSideEffect>() {

  override fun createInitialState() = FirstUiState()

  override fun handleEvent(event: FirstUiEvent) {
    when (event) {
      is FirstUiEvent.OnClickedItem -> {
        navigateTo(
          route = DetailRoute(
            id = event.item.id,
            name = event.item.name.value ?: ""
          )
        )
      }
    }
  }

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