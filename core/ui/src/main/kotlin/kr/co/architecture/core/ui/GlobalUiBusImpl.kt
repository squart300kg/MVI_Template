package kr.co.architecture.core.ui

import dagger.hilt.android.scopes.ActivityRetainedScoped
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kr.co.architecture.core.model.ArchitectureSampleHttpFailure
import kr.co.architecture.core.ui.util.UiText

@ActivityRetainedScoped
internal class GlobalUiBusImpl @Inject constructor(): GlobalUiBus {

  // 로딩은 중첩을 허용하는 카운터 방식(동시 요청 대비)
  private val _loadingCount = MutableStateFlow(0)
  override val loadingState: StateFlow<Boolean> = _loadingCount
    .map { it > 0 }
    .distinctUntilChanged()
    .stateIn(
      scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
      started = SharingStarted.WhileSubscribed(),
      initialValue = false
    )

  private val _errorDialog = MutableStateFlow<BaseCenterDialogUiModel?>(null)
  override val errorDialog = _errorDialog.asStateFlow()

  override fun showFailureDialog(
    throwable: Throwable
  ) {
    /**
     * 실무 요구사항에 따라 다양한 error case 정의 가능
     */
    val (title, contents) = when (throwable) {
      is ArchitectureSampleHttpFailure.Error -> {
        UiText.DynamicString(throwable.code) to
          UiText.DynamicString(throwable.message)
      }
      is ArchitectureSampleHttpFailure.Exception -> {
        when (throwable) {
          is ArchitectureSampleHttpFailure.Exception.NetworkConnection -> {
            UiText.StringResource(R.string.networkConnectionErrorTitle) to
              UiText.StringResource(R.string.networkConnectionErrorContents)
          }
          is ArchitectureSampleHttpFailure.Exception.Unknown -> {
            (throwable.message?.let {
              UiText.DynamicString(it)
            } ?: run {
              UiText.StringResource(R.string.unknownError)
            }) to UiText.DynamicString(throwable.stackTraceToString())
          }
        }
      }
      else -> {
        (throwable.message?.let {
          UiText.DynamicString(it)
        } ?: run {
          UiText.StringResource(R.string.unknownError)
        }) to UiText.DynamicString(throwable.stackTraceToString())
      }
    }

    _errorDialog.update {
      BaseCenterDialogUiModel(
        titleMessage = title,
        contentMessage = contents
      )
    }
  }

  override fun setLoadingState(loadingState: Boolean) {
    when (loadingState) {
      true -> _loadingCount.update { it + 1 }
      false -> _loadingCount.update { (it - 1).coerceAtLeast(0) }
    }
  }

  override fun dismissDialog() {
    _errorDialog.value = null
  }
}
