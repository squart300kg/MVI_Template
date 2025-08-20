package kr.co.architecture.feature.first

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText

enum class FirstUiType {
  NONE,
  LOADED
}

data class UiModel(
  val name: UiText
) {
  companion object {
    fun mapperToUi(names: List<String>): ImmutableList<UiModel> {
      return names
        .map {
          UiModel(
            name = UiText.DynamicString(it)
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
