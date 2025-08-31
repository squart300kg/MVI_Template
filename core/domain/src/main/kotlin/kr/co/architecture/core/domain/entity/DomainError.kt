package kr.co.architecture.core.domain.entity

sealed class DomainFailure: Exception() {
  data class Error(
    val code: String,
    override val message: String
  ): DomainFailure()
  sealed class Exception: DomainFailure() {
    data object NetworkConnection: Exception()
    data object Unknown: Exception()
  }
}