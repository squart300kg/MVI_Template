package kr.co.architecture.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.co.architecture.core.model.ArchitectureSampleHttpException
import kr.co.architecture.core.ui.util.UiText

interface UiState

interface UiEvent

interface UiSideEffect

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiSideEffect> :
  ViewModel() {

  private val initialState: State by lazy { createInitialState() }

  private val _loadingState = MutableStateFlow<Boolean>(false)
  val loadingState = _loadingState.asStateFlow()

  private val _refreshState = MutableStateFlow<Boolean>(false)
  val refreshState = _refreshState.asStateFlow()

  private val _errorMessageState = MutableSharedFlow<BaseCenterDialogUiModel>()
  val errorMessageState = _errorMessageState.asSharedFlow()

  private val _uiState = MutableStateFlow<State>(initialState)
  val uiState = _uiState.asStateFlow()

  private val _uiEvent = MutableSharedFlow<Event>()
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

  protected fun launchSafetyWithLoading(
    isPullToRefresh: Boolean = false,
    block: suspend CoroutineScope.() -> Unit,
  ) {
    viewModelScope.launch {
      if (isPullToRefresh) _refreshState.update { true }
      else _loadingState.update { true }
      runCatching {
        coroutineScope { block() }
      }.onFailure {
        it.printStackTrace()
        showErrorDialog(it) }
      if (isPullToRefresh) _refreshState.update { false }
      else _loadingState.update { false }
    }
  }

  protected suspend fun showErrorDialog(throwable: Throwable?) {
    _errorMessageState.emit(
      when (throwable) {
        is ArchitectureSampleHttpException -> {
          BaseCenterDialogUiModel(
            titleMessage = UiText.DynamicString("[${throwable.code}]"),
            contentMessage = UiText.DynamicString(throwable.message),
            confirmButtonMessage = UiText.DynamicString("확인"),
          )
        }
        else -> {
          BaseCenterDialogUiModel(
            titleMessage = UiText.DynamicString(throwable?.stackTraceToString().toString()),
            contentMessage = UiText.DynamicString(""),
            confirmButtonMessage = UiText.DynamicString("취소")
          )
        }
      }
    )
  }
}