package kr.co.architecture.yeo.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kr.co.architecture.yeo.core.domain.entity.SearchedBooks
import kr.co.architecture.yeo.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBooksUseCaseImpl @Inject constructor(
  private val bookRepository: BookRepository
): SearchBooksUseCase {
  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun invoke(params: SearchBooksUseCase.Params): SearchedBooks =
    bookRepository.searchBooks(params)
}