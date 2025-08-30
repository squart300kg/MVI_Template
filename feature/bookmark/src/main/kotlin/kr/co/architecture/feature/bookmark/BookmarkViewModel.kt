package kr.co.architecture.feature.bookmark

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.co.architecture.core.ui.util.formatter.DateTextFormatter
import kr.co.architecture.core.ui.util.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.BookmarkFilter
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.enums.SortDirectionEnum
import kr.co.architecture.core.domain.enums.SortPriceRangeEnum
import kr.co.architecture.core.domain.usecase.ObserveFilteredBookmarksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.BookUiModel
import kr.co.architecture.core.ui.DetailRoute
import kr.co.architecture.core.ui.enums.SortDirectionUiEnum
import kr.co.architecture.core.ui.enums.SortPriceRangeUiEnum
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BookmarkViewModel @Inject constructor(
  observeFilteredBookmarksUseCase: ObserveFilteredBookmarksUseCase,
  private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
  private val dateTextFormatter: DateTextFormatter,
  private val moneyTextFormatter: MoneyTextFormatter,
) : BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkUiSideEffect>() {

  private val queryFlow = MutableStateFlow("")
  private val sortDirectionFlow = MutableStateFlow(SortDirectionEnum.ASCENDING)
  private val sortPriceRangeFlow = MutableStateFlow(SortPriceRangeEnum.ALL)

  @OptIn(FlowPreview::class)
  private val filterFlow: Flow<BookmarkFilter> =
    combine(
      flow = queryFlow
        .debounce(250)
        .map { it.trim() }
        .distinctUntilChanged(),
      flow2 = sortDirectionFlow,
      flow3 = sortPriceRangeFlow
    ) { query, sortDirection, sortPriceRange ->
      BookmarkFilter(
        query = query,
        sortDirection = sortDirection,
        priceRange = sortPriceRange
      )
    }

  override fun createInitialState() = BookmarkUiState()

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
          }.onFailure { globalUiBus.showErrorDialog(it) }
        }
      }
      is BookmarkUiEvent.OnQueryChange -> {
        queryFlow.update { event.query }
      }
      is BookmarkUiEvent.OnChangeSortDirection -> {
        setState { copy(bookUiModels = persistentListOf()) }.also {
          sortDirectionFlow.update { SortDirectionUiEnum.mapperToDomain(event.uiEnum) }.also {
            setState { copy(sortDirectionUiEnum = event.uiEnum) }
          }
        }
      }
      is BookmarkUiEvent.OnChangePriceRange -> {
        viewModelScope.launch {
          setState { copy(bookUiModels = persistentListOf()) }
          delay(2000)
          sortPriceRangeFlow.update { SortPriceRangeUiEnum.mapperToDomain(event.uiEnum) }.also {
            setState { copy(sortPriceRangeUiEnum = event.uiEnum) }
          }
        }
      }
    }
  }

  init {
    observeFilteredBookmarksUseCase(filterFlow)
      .onEach { book ->
        setState {
          copy(
            uiType =
              if (book.isNotEmpty()) BookmarkUiType.LOADED_RESULT
              else BookmarkUiType.EMPTY_RESULT,
            bookUiModels = BookUiModel.mapperToUi(
              book = book,
              dateTextFormatter = dateTextFormatter,
              moneyTextFormatter = moneyTextFormatter
            )
          )
        }
      }.launchIn(viewModelScope)
  }
}