package kr.co.architecture.feature.first

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

enum class FirstUiType {
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

data class FirstUiState(
    val uiType: FirstUiType = FirstUiType.NONE,
    val uiModels: ImmutableList<UiModel> = persistentListOf(),
    val isLoading: Boolean = false
): UiState

sealed interface FirstUiEvent: UiEvent {

}

sealed interface FirstUiSideEffect: UiSideEffect {
    data object Load: FirstUiSideEffect
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
        viewModelScope.launch {
            repository.getList()
                .onStart {  }
                .onCompletion {  }
                .catch { setErrorState(it) }
                .collect {
                    setState { copy(
                        uiType = FirstUiType.LOADED,
                        uiModels = UiModel.mapperToUi(it)
                    ) }
                }
        }
    }
}