package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kr.co.architecture.core.domain.enums.SearchTypeEnum
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  suspend operator fun invoke(params: Params) =
    bookRepository.searchBooks(params)

  data class Params(
    val page: Int,
    val query: String,
    val sortTypeEnum: SortTypeEnum,
    val searchTypeEnum: SearchTypeEnum
  )
}