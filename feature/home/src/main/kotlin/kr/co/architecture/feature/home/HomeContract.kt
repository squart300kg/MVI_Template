package kr.co.architecture.feature.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText

enum class HomeUiType {
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

data class HomeUiState(
  val uiType: HomeUiType = HomeUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf(),
  val isLoading: Boolean = false
) : UiState

sealed interface HomeUiEvent : UiEvent {

}

sealed interface HomeUiSideEffect : UiSideEffect {
  data object Load : HomeUiSideEffect
}
