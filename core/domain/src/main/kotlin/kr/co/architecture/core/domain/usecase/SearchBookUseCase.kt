package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.DomainResult
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  operator fun invoke(params: Params): Book? =
    bookRepository.searchBook(params)

  data class Params(
    val isbn: ISBN
  )
}