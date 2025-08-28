package kr.co.architecture.core.domain.entity

sealed interface DomainResult<out T> {
  data object Loading : DomainResult<Nothing>
  data class Success<T>(val data: T) : DomainResult<T>
  data class Error(val throwable: Throwable) : DomainResult<Nothing>
}