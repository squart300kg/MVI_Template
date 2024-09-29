package kr.co.architecture.ui.second

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kr.co.architecture.core.BaseViewModel
import kr.co.architecture.core.UiEvent
import kr.co.architecture.core.UiSideEffect
import kr.co.architecture.core.UiState
import kr.co.architecture.repository.ArticleDto
import kr.co.architecture.repository.Repository
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
): UiState

sealed interface SecondUiEvent: UiEvent {

}

sealed interface SecondUiSideEffect: UiSideEffect {
    data object Load: SecondUiSideEffect
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
                .onStart {  }
                .onCompletion {  }
                .catch { setErrorState(it) }
                .collect {
                    setState { copy(
                        uiType = SecondUiType.LOADED,
                        uiModels = UiModel.mapperToUi(it)
                    ) }
                }
        }
    }

}