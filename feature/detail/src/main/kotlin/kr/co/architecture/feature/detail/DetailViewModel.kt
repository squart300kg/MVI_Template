package kr.co.architecture.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.DetailRoute
import kr.co.architecture.core.ui.util.UiText
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailUiState, DetailUiEvent, DetailUiSideEffect>() {

  override fun createInitialState() = DetailUiState()

  override fun handleEvent(event: DetailUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init {
    setEffect { DetailUiSideEffect.Load }
  }

  fun fetchData() {
    launchWithLoading {
      val id = savedStateHandle.toRoute<DetailRoute>().id
      val name = savedStateHandle.toRoute<DetailRoute>().name

      setState {
        copy(
          uiType = DetailUiType.LOADED,
          id = UiText.DynamicString(id),
          name = UiText.DynamicString(name)
        )
      }
    }
  }
}