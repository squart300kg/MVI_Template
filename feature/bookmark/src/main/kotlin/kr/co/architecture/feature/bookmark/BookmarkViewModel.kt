package kr.co.architecture.feature.bookmark

import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
  private val searchBooksUseCase: SearchBooksUseCase
) : BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkUiSideEffect>() {

  override fun createInitialState(): BookmarkUiState {
    return BookmarkUiState()
  }

  override fun handleEvent(event: BookmarkUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { BookmarkUiSideEffect.Load } }

  fun fetchData() {

  }
}