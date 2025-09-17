package kr.co.architecture.feature.search

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.GetSortedImagesAndVideosByRecentlyUseCase
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getSortedImagesAndVideosByRecentlyUseCase: GetSortedImagesAndVideosByRecentlyUseCase
) : BaseViewModel<SearchUiState, SearchUiEvent, SearchUiSideEffect>() {

  override fun createInitialState() = SearchUiState()

  override fun handleEvent(event: SearchUiEvent) {
    when (event) {
      is SearchUiEvent.OnClickedItem -> {
//        navigateTo(
//          route = DetailRoute(
//            id = event.item.id,
//            name = event.item.name.value ?: ""
//          )
//        )
      }
      is SearchUiEvent.OnScrolledToEnd -> {
        setEffect { SearchUiSideEffect.Load.More }
      }
    }
  }

  init { setEffect { SearchUiSideEffect.Load.First } }

  fun fetchData(loadType: SearchUiSideEffect.Load) {
    launchWithLoading {
      val page = when (loadType) {
        is SearchUiSideEffect.Load.First -> setStateAndGet { copy(page = 1) }.page
        is SearchUiSideEffect.Load.More -> setStateAndGet { copy(page = page + 1) }.page
      }
      val response = getSortedImagesAndVideosByRecentlyUseCase(
        query = ContentsQuery(
          query = "빠더너스",
          page = page
        )
      )
      setState {
        copy(
          uiType =
            if (loadType is SearchUiSideEffect.Load.First && response.contentsList.isEmpty()) SearchUiType.EMPTY_RESULT
            else SearchUiType.LOADED_RESULT,
          uiModels = UiModel.mapperToUiModel(response.contentsList),
          isEndPage = response.pageableDto.isEnd,
          page = page
        )
      }
    }
  }
}