package kr.co.architecture.feature.first

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.repository.PicsumImageRepository
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repository: PicsumImageRepository
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {

  override fun createInitialState() = HomeUiState()

  override fun handleEvent(event: HomeUiEvent) {}

  init { setEffect { HomeUiSideEffect.Load } }

  fun fetchData() {
    launchWithLoading {
      val names = repository.getPicsumImages()
      setState {
        copy(
          uiType = HomeUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}