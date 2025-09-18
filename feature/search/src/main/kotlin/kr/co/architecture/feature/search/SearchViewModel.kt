package kr.co.architecture.feature.search

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kr.co.architecture.core.domain.GetSortedImagesAndVideosByRecentlyUseCase
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getSortedImagesAndVideosByRecentlyUseCase: GetSortedImagesAndVideosByRecentlyUseCase
) : BaseViewModel<SearchUiState, SearchUiEvent, SearchUiSideEffect>() {

  private var query: String = ""
  private var pageNumber = 1

  override fun createInitialState() = SearchUiState()

  override fun handleEvent(event: SearchUiEvent) {
    when (event) {
      is SearchUiEvent.OnScrolledToEnd -> {
        setEffect { SearchUiSideEffect.Load.More }
      }
      is SearchUiEvent.OnClickedBookmark -> {

      }
      is SearchUiEvent.OnQueryChange -> {
        query = event.query
      }
      is SearchUiEvent.OnSearch -> {
        setState { copy(uiModels = persistentListOf()) }
        setEffect { SearchUiSideEffect.Load.First }
      }
      is SearchUiEvent.OnClickedItem -> {
//        navigateTo(
//          route = DetailRoute(
//            id = event.item.id,
//            name = event.item.name.value ?: ""
//          )
//        )
      }
    }
  }

  init { setEffect { SearchUiSideEffect.Load.First } }

  fun fetchData(loadType: SearchUiSideEffect.Load) {
    launchWithLoading {
      if (loadType is SearchUiSideEffect.Load.First) {
        pageNumber = 1
      }

      val nextPage = when (loadType) {
        is SearchUiSideEffect.Load.First -> 1
        is SearchUiSideEffect.Load.More -> pageNumber + 1
      }
      val response = getSortedImagesAndVideosByRecentlyUseCase(
        query = ContentsQuery(
          query = "빠더너스",
//          query = query,
          page = nextPage
        )
      )
      setState {
        copy(
          uiType =
            if (loadType is SearchUiSideEffect.Load.First && response.contentsList.isEmpty()) SearchUiType.EMPTY_RESULT
            else SearchUiType.LOADED_RESULT,
          uiModels = run {
            val pagingUiModel = UiModelState.PagingUiModel.mapperToUiModel(
              dto = response.pageableDto,
              page = nextPage
            )
            val contentsUiModels = UiModelState.ContentsUiModel.mapperToUiModel(
              contents = response.contentsList
            )
            val uiModelState = (contentsUiModels as PersistentList<UiModelState>)
              .add(pagingUiModel)

            when (loadType) {
              is SearchUiSideEffect.Load.First -> uiModelState
              is SearchUiSideEffect.Load.More -> (uiState.value.uiModels as PersistentList)
                .addAll(uiModelState)
            }
          },
          isEndPage = response.pageableDto.isEnd
        )
      }.also { pageNumber = nextPage }
    }
  }
}