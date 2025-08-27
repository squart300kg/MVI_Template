package kr.co.architecture.feature.bookmark

import kr.co.architecture.core.domain.usecase.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
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
      val names = getListUseCase(
        params = GetListUseCase.Params(
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