package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  suspend operator fun invoke(params: Params): SearchedBook {
    return bookRepository.searchBook(params)
  }

  data class Params(
    val page: Int,
    val query: String,
    val sortTypeEnum: SortTypeEnum
  )
}