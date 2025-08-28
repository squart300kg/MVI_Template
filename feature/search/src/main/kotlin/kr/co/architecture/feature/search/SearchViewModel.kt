package kr.co.architecture.feature.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.co.architecture.core.common.formatter.DateTextFormatter
import kr.co.architecture.core.common.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.enums.SearchTypeEnum
import kr.co.architecture.core.domain.usecase.ObserveBookmarkedBooksUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.DetailRoute
import kr.co.architecture.core.ui.enums.SortTypeUiEnum
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
  private val searchBooksUseCase: SearchBooksUseCase,
  private val observeBookmarkedBooksUseCase: ObserveBookmarkedBooksUseCase,
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
          runCatching {
            toggleBookmarkUseCase(
              params = ToggleBookmarkUseCase.Params(
                bookmarkToggleTypeEnum =
                  if (event.item.isBookmarked) BookmarkToggleTypeEnum.DELETE
                  else BookmarkToggleTypeEnum.SAVE,
                isbn = ISBN(event.item.isbn)
              )
            )
          }.onFailure { showErrorDialog(it) }
        }
      }
      is SearchUiEvent.OnQueryChange -> {
        setState { copy(query = event.query) }
      }
      is SearchUiEvent.OnSearch -> {
        setEffect { SearchUiSideEffect.Load.First }
      }
      is SearchUiEvent.OnChangeSort -> {
        setState { copy(sort = event.sort) }
        setEffect { SearchUiSideEffect.Load.First }
      }
    }
  }

  /**
   * 1. Search
   *   1. sorting o
   *   2. 검색 o
   *   3. 페이징 o
   *   4. 상세 페이지 이동 o
   * 2. Bookmark
   *   1. 로컬 리스트 조회
   *   2. 검색
   *   3. 북마크 해제
   *   4. 상세 페이지 이동
   */
  init {
    uiState
      .filter { it.bookUiModels.isNotEmpty() }
      .flatMapConcat { uiState ->
        observeBookmarkedBooksUseCase()
          .onEach { bookmarkedBooks ->
            setState {
              copy(
                bookUiModels = bookUiModels
                  .map { uiModel ->
                    uiModel.copy(isBookmarked = bookmarkedBooks.any { it.isbn == uiModel.isbn })
                  }.toImmutableList()
              )
            }
          }
      }.launchIn(viewModelScope)
  }

  // TODO: 검색결과 없을때도 표시
  fun fetchData(loadType: SearchUiSideEffect.Load) {
    viewModelScope.launch {
      _loadingState.update { true }
      runCatching {
        val searchedBooks = searchBooksUseCase(
          params = SearchBooksUseCase.Params(
            page = when (loadType) {
              is SearchUiSideEffect.Load.First -> setStateAndGet { copy(page = 1) }.page
              is SearchUiSideEffect.Load.More -> setStateAndGet { copy(page = page + 1) }.page
            },
            query = uiState.value.query,
            sortTypeEnum = SortTypeUiEnum.mapperToDomain(uiState.value.sort),
            searchTypeEnum = SearchTypeEnum.IN_REMOTE
          )
        )
        setState {
          copy(
            uiType = SearchUiType.LOADED,
            bookUiModels = run {
              val bookUiModel = BookUiModel.mapperToUi(
                searchedBooks = searchedBooks,
                dateTextFormatter = dateTextFormatter,
                moneyTextFormatter = moneyTextFormatter
              )
              when (loadType) {
                is SearchUiSideEffect.Load.First -> bookUiModel
                is SearchUiSideEffect.Load.More -> (uiState.value.bookUiModels as PersistentList)
                  .addAll(bookUiModel)
              }
            },
            isPageable = searchedBooks.pageable.isEnd
          )}
      }.onFailure { showErrorDialog(it) }
      _loadingState.update { false }
    }
  }
}