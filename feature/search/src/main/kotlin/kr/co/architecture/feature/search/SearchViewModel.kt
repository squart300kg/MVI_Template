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
import kotlinx.coroutines.launch
import kr.co.architecture.core.domain.entity.DomainResult
import kr.co.architecture.core.ui.util.formatter.DateTextFormatter
import kr.co.architecture.core.ui.util.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.usecase.ObserveBookmarkedBooksUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.BookUiModel
import kr.co.architecture.core.ui.DetailRoute
import kr.co.architecture.core.ui.enums.SortUiEnum
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

  private var cachedQuery: String = ""

  override fun createInitialState() = SearchUiState()

  override fun handleEvent(event: SearchUiEvent) {
    when (event) {
      is SearchUiEvent.OnScrolledToEnd -> {
        setEffect { SearchUiSideEffect.Load.More }
      }
      is SearchUiEvent.OnClickedItem -> {
        navigateTo(
          route = DetailRoute(
            isbn = event.isbn
          )
        )
      }
      is SearchUiEvent.OnClickedBookmark -> {
        viewModelScope.launch {
          runCatching {
            toggleBookmarkUseCase(
              params = ToggleBookmarkUseCase.Params(
                bookmarkToggleTypeEnum =
                  if (event.isBookmarked) BookmarkToggleTypeEnum.DELETE
                  else BookmarkToggleTypeEnum.SAVE,
                isbn = ISBN(event.isbn)
              )
            )
          }.onFailure { globalUiBus.showErrorDialog(it) }
        }
      }
      is SearchUiEvent.OnQueryChange -> {
        cachedQuery = event.query
      }
      is SearchUiEvent.OnSearch -> {
        setEffect { SearchUiSideEffect.Load.First }
      }
      is SearchUiEvent.OnChangeSort -> {
        setState { copy(sortUiEnum = event.sort) }
        setEffect { SearchUiSideEffect.Load.First }
      }
    }
  }

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

  fun fetchData(loadType: SearchUiSideEffect.Load) {
    viewModelScope.launch {
      globalUiBus.setLoadingState(true)
      val searchedBooks = searchBooksUseCase(
        params = SearchBooksUseCase.Params(
          page = when (loadType) {
            is SearchUiSideEffect.Load.First -> setStateAndGet { copy(page = 1) }.page
            is SearchUiSideEffect.Load.More -> setStateAndGet { copy(page = page + 1) }.page
          },
          query = cachedQuery,
          sortEnum = SortUiEnum.mapperToDomain(uiState.value.sortUiEnum)
        )
      )
      when (searchedBooks) {
        is DomainResult.Error -> {
          globalUiBus.showErrorDialog(searchedBooks)
        }
        is DomainResult.Success -> {
          setState {
            copy(
              uiType =
                if (loadType is SearchUiSideEffect.Load.First && searchedBooks.data.books.isEmpty()) SearchUiType.EMPTY_RESULT
                else SearchUiType.LOADED_RESULT,
              bookUiModels = run {
                val bookUiModel = BookUiModel.mapperToUi(
                  searchedBooks = searchedBooks.data,
                  dateTextFormatter = dateTextFormatter,
                  moneyTextFormatter = moneyTextFormatter
                )
                when (loadType) {
                  is SearchUiSideEffect.Load.First -> bookUiModel
                  is SearchUiSideEffect.Load.More -> (uiState.value.bookUiModels as PersistentList)
                    .addAll(bookUiModel)
                }
              },
              isPageable = searchedBooks.data.pageable.isEnd)
          }
        }
      }
      globalUiBus.setLoadingState(false)
    }
  }
}