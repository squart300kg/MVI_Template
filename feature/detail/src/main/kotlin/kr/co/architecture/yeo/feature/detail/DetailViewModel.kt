package kr.co.architecture.yeo.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.co.architecture.yeo.core.domain.entity.ISBN
import kr.co.architecture.yeo.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.yeo.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.yeo.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.yeo.core.ui.BaseViewModel
import kr.co.architecture.yeo.core.ui.DetailRoute
import kr.co.architecture.yeo.core.ui.util.formatter.DateTextFormatter
import kr.co.architecture.yeo.core.ui.util.formatter.MoneyTextFormatter
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val searchBookUseCase: SearchBookUseCase,
  private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
  private val dateTextFormatter: DateTextFormatter,
  private val moneyTextFormatter: MoneyTextFormatter
) : BaseViewModel<DetailUiState, DetailUiEvent, DetailUiSideEffect>() {

  override fun createInitialState() = DetailUiState()

  override fun handleEvent(event: DetailUiEvent) {
    when (event) {
      is DetailUiEvent.OnClickedBookmark -> {
        viewModelScope.launch {
          runCatching {
            toggleBookmarkUseCase(
              params = ToggleBookmarkUseCase.Params(
                bookmarkToggleTypeEnum =
                  if (uiState.value.isBookmarked) BookmarkToggleTypeEnum.DELETE
                  else BookmarkToggleTypeEnum.SAVE,
                isbn = ISBN(uiState.value.isbn)
              )
            )
            setState { copy(isBookmarked = !uiState.value.isBookmarked) }
          }.onFailure { globalUiBus.showFailureDialog(it) }
        }
      }
      is DetailUiEvent.OnClickedBack -> {
        viewModelScope.launch {
          navigator.navigateBack()
        }
      }
    }
  }

  init {
    viewModelScope.launch {
      runCatching {
        val isbn = savedStateHandle.toRoute<DetailRoute>().isbn
        val book = searchBookUseCase(SearchBookUseCase.Params(ISBN(isbn)))
        checkNotNull(book) {
          "cannot found book mapped with receiving isbn($isbn)"
        }
        setState {
          DetailUiState.mapperToUi(
            book = book,
            dateTextFormatter = dateTextFormatter,
            moneyTextFormatter = moneyTextFormatter
          )
        }
      }.onFailure { globalUiBus.showFailureDialog(it) }
    }
  }
}