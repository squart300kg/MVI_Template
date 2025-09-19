package kr.co.architecture.feature.detail

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.ui.BaseViewModel
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
    val id = savedStateHandle.get<String>(ARG_ID) ?: return
    // TODO: 실제 로드 (repo 등)
    setState {
      copy(
        uiType = DetailUiType.LOADED,
        id = UiText.DynamicString(id),
        name = UiText.DynamicString("타이틀입니다...") // 예시
      )
    }
  }

  companion object {
    const val ARG_ID = "id"
  }
}