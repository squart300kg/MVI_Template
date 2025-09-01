package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.enums.SortEnum

interface SearchBooksUseCase {
  suspend operator fun invoke(params: Params): SearchedBooks

  data class Params(
    val page: Int,
    val query: String,
    val sortEnum: SortEnum
  )
}