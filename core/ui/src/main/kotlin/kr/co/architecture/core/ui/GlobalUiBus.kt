package kr.co.architecture.core.ui

import kotlinx.coroutines.flow.StateFlow

interface GlobalUiBus {
  val loadingState: StateFlow<Boolean>
  val errorDialog: StateFlow<BaseCenterDialogUiModel?>
  fun showFailureDialog(throwable: Throwable)
  fun setLoadingState(loadingState: Boolean)
  fun dismissDialog()
}