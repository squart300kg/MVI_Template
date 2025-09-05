package kr.co.architecture.feature.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kr.co.architecture.core.repository.PicsumImageRepository
import kr.co.architecture.core.repository.dto.PicsumImagesDtoRequest
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repository: PicsumImageRepository
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {

  override fun createInitialState() = HomeUiState()

  override fun handleEvent(event: HomeUiEvent) {
    when (event) {
      HomeUiEvent.OnScrolledToEnd -> setEffect { HomeUiSideEffect.Load }
    }
  }

  init { setEffect { HomeUiSideEffect.Load } }

  fun fetchData(requestSize: Int) {
    launchWithLoading {
      val nextPage = uiState.value.page + 1
      val dtoResponse = repository.getPicsumImages(
        dtoRequest = PicsumImagesDtoRequest(
          page = nextPage,
          requestSize = requestSize
        )
      )
      setState {
        copy(
          uiType = HomeUiType.LOADED,
          uiModels = (uiModels as PersistentList)
            .addAll(UiModel.mapperToUi(dtoResponse)),
          page = nextPage,
          hasNext = dtoResponse.hasNext
        )
      }
    }
  }
}