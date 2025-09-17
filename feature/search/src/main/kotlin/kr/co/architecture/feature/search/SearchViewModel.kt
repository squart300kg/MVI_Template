package kr.co.architecture.feature.search

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.GetListUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.DetailRoute
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<SearchUiState, SearchUiEvent, SearchUiSideEffect>() {

  override fun createInitialState() = SearchUiState()

  override fun handleEvent(event: SearchUiEvent) {
    when (event) {
      is SearchUiEvent.OnClickedItem -> {
        navigateTo(
          route = DetailRoute(
            id = event.item.id,
            name = event.item.name.value ?: ""
          )
        )
      }
    }
  }

  init { setEffect { SearchUiSideEffect.Load } }

  fun fetchData() {
    launchWithLoading {
      val names = getListUseCase()
      setState {
        copy(
          uiType = SearchUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}