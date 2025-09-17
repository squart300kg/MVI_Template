package kr.co.architecture.feature.search

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText

enum class SearchUiType {
  NONE,
  LOADED
}

data class UiModel(
  val id: String,
  val name: UiText
) {
  companion object {
    fun mapperToUi(names: List<String>): ImmutableList<UiModel> {
      return names
        .mapIndexed { index, name ->
          UiModel(
            id = "$index",
            name = UiText.DynamicString(name)
          )
        }
        .toImmutableList()
    }
  }
}

data class SearchUiState(
  val uiType: SearchUiType = SearchUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf()
) : UiState

sealed interface SearchUiEvent : UiEvent {
  data class OnClickedItem(val item: UiModel) : SearchUiEvent
}

sealed interface SearchUiSideEffect : UiSideEffect {
  data object Load : SearchUiSideEffect
}
