package kr.co.architecture.yeo.core.domain.entity

/**
 * 실무 진행 시엔, 실제 발생 가능 case들 추가 정리 가능
 */
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