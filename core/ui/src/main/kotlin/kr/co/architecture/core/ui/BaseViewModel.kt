package kr.co.architecture.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.co.architecture.core.model.ArchitectureSampleHttpException

interface UiState

interface UiEvent

interface UiSideEffect

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiSideEffect> :
    ViewModel() {

    private val initialState: State by lazy { createInitialState() }

    private val _errorMessageState: MutableStateFlow<CenterErrorDialogMessage?> = MutableStateFlow(null)
    val errorMessageState = _errorMessageState.asStateFlow()

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiSideEffect: Channel<Effect> = Channel()
    val uiSideEffect = _uiSideEffect.receiveAsFlow()

    init {
        // subscribe event
        viewModelScope.launch {
            uiEvent.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun createInitialState(): State

    abstract fun handleEvent(event: Event)

    fun setEvent(event: Event) {
        val newEvent = event
        viewModelScope.launch { _uiEvent.emit(newEvent) }
    }

    protected fun setState(reduce: State.() -> State) {
        val newState = uiState.value.reduce()
        _uiState.update { newState }
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _uiSideEffect.send(effectValue) }
    }

    protected fun setErrorState(throwable: Throwable?) {
        _errorMessageState.update {
            when (throwable) {
                is ArchitectureSampleHttpException -> {
                    CenterErrorDialogMessage(
                        errorCode = throwable.code,
                        titleMessage = "[${throwable.code}]",
                        contentMessage = throwable.message,
                        confirmButtonMessage = "확인",
                    )
                }
                else -> {
                    CenterErrorDialogMessage(
                        errorCode = -1,
                        titleMessage = throwable?.stackTraceToString().toString(),
                        contentMessage = "",
                        confirmButtonMessage = "취소"
                    )
                }
            }
        }
    }
}