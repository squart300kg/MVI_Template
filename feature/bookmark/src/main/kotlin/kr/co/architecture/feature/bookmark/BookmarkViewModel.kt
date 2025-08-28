package kr.co.architecture.feature.bookmark

import androidx.lifecycle.viewModelScope
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.co.architecture.core.common.formatter.DateTextFormatter
import kr.co.architecture.core.common.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.usecase.ObserveBookmarkedBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.BookUiModel
import kr.co.architecture.core.ui.DetailRoute
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
  private val observeBookmarkedBooksUseCase: ObserveBookmarkedBooksUseCase,
  private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
  private val dateTextFormatter: DateTextFormatter,
  private val moneyTextFormatter: MoneyTextFormatter,
) : BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkUiSideEffect>() {

  override fun createInitialState(): BookmarkUiState {
    return BookmarkUiState()
  }

  override fun handleEvent(event: BookmarkUiEvent) {
    when (event) {
      is BookmarkUiEvent.OnClickedItem -> {
        navigateTo(
          route = DetailRoute(
            isbn = event.isbn
          )
        )
      }
      is BookmarkUiEvent.OnClickedBookmark -> {
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
          }.onFailure { showErrorDialog(it) }
        }

      }
      is BookmarkUiEvent.OnQueryChange -> {
        setState {
          copy(searchHeaderUiModel = uiState.value.searchHeaderUiModel.copy(query = { event.query }))
        }
      }
      is BookmarkUiEvent.OnSearch -> {
        setEffect { BookmarkUiSideEffect.Load }
      }
      is BookmarkUiEvent.OnChangeSort -> {
        setState {
          copy(searchHeaderUiModel = uiState.value.searchHeaderUiModel.copy(sort = event.sort))
        }
      }
    }
  }

  init {
    observeBookmarkedBooksUseCase()
      .onEach { book ->
        setState {
          copy(
            uiType = BookmarkUiType.LOADED,
            bookUiModels = BookUiModel.mapperToUi(
              book = book,
              dateTextFormatter = dateTextFormatter,
              moneyTextFormatter = moneyTextFormatter
            )
          )
        }
      }.launchIn(viewModelScope)
  }

  fun fetchData() {

  }
}