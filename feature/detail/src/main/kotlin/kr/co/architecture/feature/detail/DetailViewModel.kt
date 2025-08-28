package kr.co.architecture.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.co.architecture.core.common.formatter.DateTextFormatter
import kr.co.architecture.core.common.formatter.MoneyTextFormatter
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.DetailRoute
import kr.co.architecture.core.ui.util.UiText
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val searchBookUseCase: SearchBookUseCase,
  private val dateTextFormatter: DateTextFormatter,
  private val moneyTextFormatter: MoneyTextFormatter
) : BaseViewModel<DetailUiState, DetailUiEvent, DetailUiSideEffect>() {

  override fun createInitialState(): DetailUiState {
    return DetailUiState()
  }

  override fun handleEvent(event: DetailUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init {
    setEffect { DetailUiSideEffect.Load }
  }

  fun fetchData() {
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

      }.onFailure { showErrorDialog(it) }
    }
  }
}