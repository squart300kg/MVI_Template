package kr.co.architecture.feature.second

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
class SecondViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase
) : BaseViewModel<SecondUiState, SecondUiEvent, SecondUiSideEffect>() {

  override fun createInitialState(): SecondUiState {
    return SecondUiState()
  }

  override fun handleEvent(event: SecondUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init { setEffect { SecondUiSideEffect.Load } }

  fun fetchData() {
    launchSafetyWithLoading {
      val names = getListUseCase()
      setState {
        copy(
          uiType = SecondUiType.LOADED,
          uiModels = UiModel.mapperToUi(names)
        )
      }
    }
  }
}