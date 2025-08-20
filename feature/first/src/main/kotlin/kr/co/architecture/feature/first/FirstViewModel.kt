package kr.co.architecture.feature.first

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.repository.Repository
import kr.co.architecture.core.repository.dto.ArticleDto
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText
import javax.inject.Inject

enum class FirstUiType {
  NONE,
  LOADED
}

data class UiModel(
  val name: UiText
) {
  companion object {
    fun mapperToUi(dtos: List<ArticleDto>): ImmutableList<UiModel> {
      return dtos
        .map {
          UiModel(
            name = UiText.DynamicString(it.name)
          )
        }
        .toImmutableList()
    }
  }
}

data class FirstUiState(
  val uiType: FirstUiType = FirstUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf(),
  val isLoading: Boolean = false
) : UiState

sealed interface FirstUiEvent : UiEvent {

}

sealed interface FirstUiSideEffect : UiSideEffect {
  data object Load : FirstUiSideEffect
}

@HiltViewModel
class FirstViewModel @Inject constructor(
  private val repository: Repository,
) : BaseViewModel<FirstUiState, FirstUiEvent, FirstUiSideEffect>() {

  override fun createInitialState(): FirstUiState {
    return FirstUiState()
  }

  override fun handleEvent(event: FirstUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init {
    setEffect { FirstUiSideEffect.Load }
  }

  fun fetchData() {
    launchSafetyWithLoading {
      val dto = repository.getList()
      setState {
        copy(
          uiType = FirstUiType.LOADED,
          uiModels = UiModel.mapperToUi(dto)
        )
      }
    }
  }
}