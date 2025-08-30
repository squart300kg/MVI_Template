package kr.co.architecture.core.domain.entity

sealed interface DomainResult<out T> {
  data class Success<T>(val data: T) : DomainResult<T>
  data class Error(
    val errorCode: String,
    val errorMessage: String
  ) : DomainResult<Nothing>, Exception(errorMessage) {
    /**
     * 실무 요구사항에 따라 다양한 error case 정의 가능
     */
  }
}