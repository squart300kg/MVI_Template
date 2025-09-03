package kr.co.architecture.core.network.model

// TODO: 헤더를 Map에서 객체 타입으로 전환하기
sealed interface ApiResponse<out T> {
  data class Success<T>(
    val data: T,
    val header: Map<String, String>,
    val code: Int,
    val message: String
  ) : ApiResponse<T>

  data class Error(
    val code: Int,
    val message: String,
    val errorBody: String
  ) : ApiResponse<Nothing>

  data class Exception(
    val throwable: Throwable
  ) : ApiResponse<Nothing>
}