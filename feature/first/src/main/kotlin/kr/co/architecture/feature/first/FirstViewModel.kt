package kr.co.architecture.feature.first

import kr.co.architecture.core.domain.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<FirstUiState, FirstUiEvent, FirstUiSideEffect>() {

  override fun createInitialState(): FirstUiState {
    return FirstUiState()
  }

  override fun handleEvent(event: FirstUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { FirstUiSideEffect.Load } }

  fun fetchData() {
    launchSafetyWithLoading {
      val names = getListUseCase()
      setState {
        copy(
          uiType = FirstUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}