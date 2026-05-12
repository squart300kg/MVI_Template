package kr.co.architecture.feature.second

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

enum class SecondUiType {
  NONE,
  LOADED
}

data class UiModel(
  val name: String
) {
  companion object {
    fun mapperToUi(names: List<String>): ImmutableList<UiModel> {
      return names
        .map {
          UiModel(
            name = it
          )
        }
        .toImmutableList()
    }
  }
}

data class SecondUiState(
  val uiType: SecondUiType = SecondUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf()
) : UiState

sealed interface SecondUiEvent : UiEvent {

}

sealed interface SecondUiSideEffect : UiSideEffect {
  data object Load : SecondUiSideEffect
}
