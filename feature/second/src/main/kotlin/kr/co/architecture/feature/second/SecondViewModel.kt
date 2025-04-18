package kr.co.architecture.feature.second

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.repository.Repository
import kr.co.architecture.core.repository.dto.ArticleDto
import javax.inject.Inject

enum class SecondUiType {
  NONE,
  LOADED
}

data class UiModel(
  val name: String
) {
  companion object {
    fun mapperToUi(dtos: List<ArticleDto>): ImmutableList<UiModel> {
      return dtos
        .map { UiModel(it.name) }
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

@HiltViewModel
class SecondViewModel @Inject constructor(
  private val repository: Repository,
) : BaseViewModel<SecondUiState, SecondUiEvent, SecondUiSideEffect>() {

  override fun createInitialState(): SecondUiState {
    return SecondUiState()
  }

  override fun handleEvent(event: SecondUiEvent) {
    when (event) {
      else -> {}
    }
  }

  init {
    setEffect { SecondUiSideEffect.Load }
  }

  fun fetchData() {
    viewModelScope.launch {
      repository.getList()
        .onStart { }
        .onCompletion { }
        .catch { setErrorState(it) }
        .collect {
          setState {
            copy(
              uiType = SecondUiType.LOADED,
              uiModels = UiModel.mapperToUi(it)
            )
          }
        }
    }
  }

}