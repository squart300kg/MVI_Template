package kr.co.architecture.core.network.model

data class ApiResponse<out T>(
  val code: Int,
  val message: String,
  val header: Map<String, String>,
  val body: T
)