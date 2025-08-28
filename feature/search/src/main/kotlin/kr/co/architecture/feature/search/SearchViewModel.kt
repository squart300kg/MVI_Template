package kr.co.architecture.feature.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.co.architecture.core.common.formatter.DateTextFormatter
import kr.co.architecture.core.common.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.DomainResult
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.enums.SearchTypeEnum
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.DetailRoute
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val searchBooksUseCase: SearchBooksUseCase,
  private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
  private val dateTextFormatter: DateTextFormatter,
  private val moneyTextFormatter: MoneyTextFormatter,
) : BaseViewModel<SearchUiState, SearchUiEvent, SearchUiSideEffect>() {

  override fun createInitialState(): SearchUiState {
    return SearchUiState()
  }

  override fun handleEvent(event: SearchUiEvent) {
    when (event) {
      is SearchUiEvent.OnScrolledToEnd -> {
        setEffect { SearchUiSideEffect.Load.More }
      }
      is SearchUiEvent.OnClickedItem -> {
        navigateTo(
          route = DetailRoute(
            isbn = event.item.isbn
          )
        )
      }
      is SearchUiEvent.OnClickedBookmark -> {
        viewModelScope.launch {

          toggleBookmarkUseCase(
            params = ToggleBookmarkUseCase.Params(
              bookmarkToggleTypeEnum =
                if (event.item.isBookmarked) BookmarkToggleTypeEnum.DELETE
                else BookmarkToggleTypeEnum.SAVE,
              isbn = ISBN(event.item.isbn)
            )
          )
        }
      }
    }
  }

  /**
   * 1. Search
   *   1. sorting
   *   2. 검색
   *   3. 페이징 o
   *   4. 상세 페이지 이동
   * 2. Bookmark
   *   1. 로컬 리스트 조회
   *   2. 검색
   *   3. 북마크 해제
   *   4. 상세 페이지 이동
   */

  init {
    setEffect { SearchUiSideEffect.Load.First }
  }

  // TODO: 검색결과 없을때도 표시
  fun fetchData(loadType: SearchUiSideEffect.Load) {
    viewModelScope.launch {
      searchBooksUseCase(
        params = SearchBooksUseCase.Params(
          page = when (loadType) {
            is SearchUiSideEffect.Load.First -> 1
            is SearchUiSideEffect.Load.More -> setStateAndGet { copy(page = page + 1) }.page
          },
          query = "미움받을용기",
          sortTypeEnum = SortTypeEnum.ACCURACY,
          searchTypeEnum = SearchTypeEnum.IN_REMOTE
        )
      ).collect { result ->
        when (result) {
          is DomainResult.Loading -> {
            _loadingState.update { true }
          }
          is DomainResult.Error -> {
            _loadingState.update { false }
            showErrorDialog(result.throwable)
          }
          is DomainResult.Success -> {
            _loadingState.update { false }
            setState {
              copy(
              uiType = SearchUiType.LOADED,
              uiModels = run {
                val uiModel = UiModel.mapperToUi(
                  searchedBooks = result.data,
                  dateTextFormatter = dateTextFormatter,
                  moneyTextFormatter = moneyTextFormatter
                )
                when (loadType) {
                  is SearchUiSideEffect.Load.First -> uiModel
                  is SearchUiSideEffect.Load.More -> (uiState.value.uiModels as PersistentList)
                    .addAll(uiModel)
                }
              },
              isPageable = result.data.pageable.isEnd
            )}
          }
        }
      }
    }
  }
}