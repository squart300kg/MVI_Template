package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.enums.SortEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  suspend operator fun invoke(params: Params): SearchedBooks =
    bookRepository.searchBooks(params)

  data class Params(
    val page: Int,
    val query: String,
    val sortEnum: SortEnum
  )
}