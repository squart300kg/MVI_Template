package kr.co.architecture.core.model

sealed class ArchitectureSampleHttpFailure: Exception() {
  data class Error(
    val code: String,
    override val message: String
  ): ArchitectureSampleHttpFailure()
  sealed class Exception: ArchitectureSampleHttpFailure() {
    data object NetworkConnection: Exception()
    data object Unknown: Exception()
  }
}