package kr.co.architecture.core.network.model

sealed interface ApiResponse<out S, out E> {
  data class Success<S>(
    val data: S,
    val header: Map<String, String>,
    val code: Int,
    val message: String
  ) : ApiResponse<S, Nothing>

  data class Error<E>(
    val data: E,
    val header: Map<String, String>
  ) : ApiResponse<Nothing, E>

  data class Exception(
    val throwable: Throwable
  ) : ApiResponse<Nothing, Nothing>
}