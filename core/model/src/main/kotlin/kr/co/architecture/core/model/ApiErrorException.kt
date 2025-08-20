package kr.co.architecture.core.model

sealed class ApiException(message: String): Exception(message) {
    data class ApiOnError(
        val code: Int,
        override val message: String,
    ): ApiException(message)
    data class ApiOnException(
        val throwable: Throwable,
        override val message: String,
    ): ApiException(message)
}
