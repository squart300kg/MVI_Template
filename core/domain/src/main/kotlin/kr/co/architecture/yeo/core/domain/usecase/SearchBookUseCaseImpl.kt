package kr.co.architecture.yeo.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kr.co.architecture.yeo.core.domain.entity.Book
import kr.co.architecture.yeo.core.domain.entity.ISBN
import kr.co.architecture.yeo.core.domain.repository.BookRepository
import kr.co.architecture.yeo.core.domain.usecase.SearchBookUseCase.Params
import javax.inject.Inject

class SearchBookUseCaseImpl @Inject constructor(
  private val bookRepository: BookRepository
): SearchBookUseCase {
  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend operator fun invoke(params: Params): Book? =
    bookRepository.searchBook(params)
}