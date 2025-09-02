package kr.co.architecture.feature.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kr.co.architecture.core.repository.PicsumImageRepository
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

  fun fetchData() {
    launchWithLoading {
      val nextPage = uiState.value.page + 1
      val dto = repository.getPicsumImages(nextPage)
      println("pagingLog : ${dto.items.map { it.id to dto.hasNext }}")
      setState {
        copy(
          uiType = HomeUiType.LOADED,
          uiModels = (uiModels as PersistentList)
            .addAll(UiModel.mapperToUi(dto)),
          page = nextPage,
          hasNext = dto.hasNext
        )
      }
    }
  }
}