package kr.co.architecture.yeo.core.domain.usecase

import kr.co.architecture.yeo.core.domain.repository.BookRepository
import javax.inject.Inject

class ObserveBookmarkedBooksUseCaseImpl @Inject constructor(
  private val bookRepository: BookRepository
): ObserveBookmarkedBooksUseCase {
  override operator fun invoke() =
    bookRepository.observeBookmarkedBooks()

}