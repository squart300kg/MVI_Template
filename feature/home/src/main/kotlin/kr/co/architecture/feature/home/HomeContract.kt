package kr.co.architecture.feature.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.repository.dto.PicsumImagesDtoResponse
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState

enum class HomeUiType {
  NONE,
  LOADED
}

data class UiModel(
  val image: String
) {
  companion object {
    fun mapperToUi(dto: PicsumImagesDtoResponse): ImmutableList<UiModel> {
      return dto.items
        .map { UiModel(it.downloadUrl) }
        .toImmutableList()
    }
  }
}

data class HomeUiState(
  val uiType: HomeUiType = HomeUiType.NONE,
  val uiModels: ImmutableList<UiModel> = persistentListOf(),
  val page: Int = 1,
  val hasNext: Boolean = false
) : UiState

sealed interface HomeUiEvent : UiEvent {
  data object OnScrolledToEnd : HomeUiEvent
}

sealed interface HomeUiSideEffect : UiSideEffect {
  data object Load: HomeUiSideEffect
}
