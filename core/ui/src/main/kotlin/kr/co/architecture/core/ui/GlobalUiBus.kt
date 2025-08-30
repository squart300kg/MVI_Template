package kr.co.architecture.core.ui

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kr.co.architecture.core.domain.entity.DomainResult
import kr.co.architecture.core.ui.util.UiText

class GlobalUiBus @Inject constructor() {

  // 로딩은 중첩을 허용하는 카운터 방식(동시 요청 대비)
  private val _loadingCount = MutableStateFlow(0)
  val loadingState: StateFlow<Boolean> = _loadingCount
    .map { it > 0 }
    .stateIn(
      scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
      started = SharingStarted.Eagerly,
      initialValue = false
    )

  private val _errorDialog = MutableStateFlow<BaseCenterDialogUiModel?>(null)
  val errorDialog = _errorDialog.asStateFlow()

  fun showErrorDialog(
    throwable: Throwable,
  ) {
    /**
     * 실무 요구사항에 따라 다양한 error case 정의 가능
     */
    val (title, content) = when (throwable) {
      is DomainResult.Error -> throwable.errorCode to throwable.errorMessage
      else -> throwable.message to throwable.stackTraceToString()
    }
    _errorDialog.update {
      BaseCenterDialogUiModel(
        titleMessage = UiText.DynamicString("[$title]"),
        contentMessage = UiText.DynamicString("$content]"),
        confirmButtonMessage = UiText.StringResource(R.string.confirm)
      )
    }
  }

  fun setLoadingState(loadingState: Boolean) {
    when (loadingState) {
      true -> _loadingCount.update { it + 1 }
      false -> _loadingCount.update { (it - 1).coerceAtLeast(0) }
    }
  }

  fun dismissDialog() {
    _errorDialog.value = null
  }
}
