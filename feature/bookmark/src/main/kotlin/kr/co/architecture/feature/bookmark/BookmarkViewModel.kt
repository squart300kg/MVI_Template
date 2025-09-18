package kr.co.architecture.feature.bookmark

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.architecture.core.domain.ObserveBookmarkedMediasUseCase
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
  private val observeBookmarkedMediasUseCase: ObserveBookmarkedMediasUseCase
) : BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkUiSideEffect>() {

  override fun createInitialState() = BookmarkUiState()

  override fun handleEvent(event: BookmarkUiEvent) {
    when (event) {
      is BookmarkUiEvent.OnClickedItem -> {

      }
      is BookmarkUiEvent.OnClickedBookmark -> {

      }
    }
  }

  init {
    observeBookmarkedMediasUseCase()
      .onEach { book ->
        setState {
          copy(
            uiType =
              if (book.isNotEmpty()) BookmarkUiType.LOADED_RESULT
              else BookmarkUiType.EMPTY_RESULT,
            uiModels = UiModel.mapperToUiModel(book)
          )
        }
      }.launchIn(viewModelScope)
  }
}