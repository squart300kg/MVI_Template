package kr.co.architecture.feature.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.usecase.GetListUseCase
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.DetailRoute
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {

  override fun createInitialState(): HomeUiState {
    return HomeUiState()
  }

  override fun handleEvent(event: HomeUiEvent) {
    when (event) {
      is HomeUiEvent.OnClickedItem -> {
        navigateTo(
          route = DetailRoute(
            id = event.item.id,
            name = event.item.name.value ?: ""
          )
        )
      }
    }
  }

  init { setEffect { HomeUiSideEffect.Load } }

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
//          uiType = FirstUiType.LOADED,
//          uiModels = UiModel.mapperToUi(names)
//        )
//      }
    }
  }
}