package kr.co.architecture.core.model

sealed interface UiResult<out T> {
    data object Loading: UiResult<Nothing>
    data object Empty: UiResult<Nothing>
    data class Success<T>(val model: T):
        UiResult<T>
    data class Error(val throwable: Throwable):
        UiResult<Nothing>
}