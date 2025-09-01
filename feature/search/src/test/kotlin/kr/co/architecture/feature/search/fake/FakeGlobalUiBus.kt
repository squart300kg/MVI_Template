package kr.co.architecture.feature.search.fake

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.co.architecture.core.ui.BaseCenterDialogUiModel
import kr.co.architecture.core.ui.GlobalUiBus
import kr.co.architecture.core.ui.util.UiText

class FakeGlobalUiBus : GlobalUiBus {
  private val _loading = MutableStateFlow(false)
  private val _dialog = MutableStateFlow<BaseCenterDialogUiModel?>(null)
  val loadingHistory = mutableListOf<Boolean>() // 검증용

  override val loadingState: StateFlow<Boolean> = _loading
  override val errorDialog: StateFlow<BaseCenterDialogUiModel?> = _dialog

  override fun showFailureDialog(throwable: Throwable) {
    _dialog.value = BaseCenterDialogUiModel(
      titleMessage = UiText.DynamicString(throwable.message ?: "error"),
      contentMessage = UiText.DynamicString(throwable.stackTraceToString())
    )
  }

  override fun setLoadingState(loadingState: Boolean) {
    loadingHistory += loadingState
    _loading.value = loadingState
  }

  override fun dismissDialog() { _dialog.value = null }
  }