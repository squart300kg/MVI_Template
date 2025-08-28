package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class ObserveBookmarkedBooksUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  operator fun invoke() =
    bookRepository.observeBookmarkedBooks()

}