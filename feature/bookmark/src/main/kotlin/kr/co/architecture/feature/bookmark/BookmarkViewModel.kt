package kr.co.architecture.feature.bookmark

import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
  private val searchBookUseCase: SearchBookUseCase
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
    launchSafetyWithLoading {
      val names = searchBookUseCase(
        params = SearchBookUseCase.Params(
          page = 1,
          query = "미움받을용기",
          sortTypeEnum = SortTypeEnum.ACCURACY
        )
      )
      println("apiLog : $names")
//      setState {
//        copy(
//          uiType = SecondUiType.LOADED,
//          uiModels = UiModel.mapperToUi(names)
//        )
//      }
    }
  }
}