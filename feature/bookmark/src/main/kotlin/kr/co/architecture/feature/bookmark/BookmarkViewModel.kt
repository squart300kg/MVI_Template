package kr.co.architecture.feature.bookmark

import kr.co.architecture.core.domain.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkUiSideEffect>() {

  override fun createInitialState() = BookmarkUiState()

  override fun handleEvent(event: BookmarkUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { BookmarkUiSideEffect.Load } }

  fun fetchData() {
    launchWithLoading {
      val names = getListUseCase()
      setState {
        copy(
          uiType = BookmarkUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}