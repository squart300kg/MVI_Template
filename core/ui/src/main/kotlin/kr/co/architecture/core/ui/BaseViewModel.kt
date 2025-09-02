package kr.co.architecture.core.ui

import androidx.annotation.VisibleForTesting
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
import javax.inject.Inject

interface UiState

interface UiEvent

interface UiSideEffect

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiSideEffect> :
  ViewModel() {

  private val initialState: State by lazy { createInitialState() }

  private val _uiState = MutableStateFlow<State>(initialState)
  val uiState = _uiState.asStateFlow()

  private val _uiEvent = MutableSharedFlow<Event>()
  val uiEvent = _uiEvent.asSharedFlow()

  private val _uiSideEffect: Channel<Effect> = Channel()
  val uiSideEffect = _uiSideEffect.receiveAsFlow()

  @Inject
  lateinit var globalUiBus: GlobalUiBus

  @VisibleForTesting
  fun injectGlobalUiBus(globalUiBus: GlobalUiBus) {
    this.globalUiBus = globalUiBus
  }

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

  fun <T> launchWithLoading(block: suspend () -> T) {
    viewModelScope.launch {
      try {
        globalUiBus.setLoadingState(true)
        block()
      } catch (e: Exception) {
        globalUiBus.showFailureDialog(e)
      } finally {
        globalUiBus.setLoadingState(false)
      }
    }
  }
}