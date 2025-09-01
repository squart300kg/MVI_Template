package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookUseCaseImpl @Inject constructor(
  private val bookRepository: BookRepository
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  suspend operator fun invoke(params: Params): Book? =
    bookRepository.searchBook(params)

  data class Params(
    val isbn: ISBN
  )
}