package kr.co.architecture.feature.search

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.common.date.DateTextFormatter
import kr.co.architecture.core.domain.enums.SearchTypeEnum
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val searchBookUseCase: SearchBookUseCase,
  private val dateTextFormatter: DateTextFormatter
) : BaseViewModel<SearchUiState, SearchUiEvent, HomeUiSideEffect>() {

  override fun createInitialState(): SearchUiState {
    return SearchUiState()
  }

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
      is SearchUiEvent.OnClickedBookmark -> {
//        navigateTo(
//          route = DetailRoute(
//            id = event.item.id,
//            name = event.item.name.value ?: ""
//          )
//        )
      }
    }
  }

  init { setEffect { HomeUiSideEffect.Load } }

  fun fetchData() {
    launchSafetyWithLoading {
      val searchedBook = searchBookUseCase(
        params = SearchBookUseCase.Params(
          page = 1,
          query = "미움받을용기",
          sortTypeEnum = SortTypeEnum.ACCURACY,
          searchTypeEnum = SearchTypeEnum.IN_REMOTE
        )
      )

      setState {
        copy(
          uiType = SearchUiType.LOADED,
          uiModels = UiModel.mapperToUi(
            searchedBook = searchedBook,
            dateTextFormatter = dateTextFormatter
          )
        )
      }
    }
  }
}