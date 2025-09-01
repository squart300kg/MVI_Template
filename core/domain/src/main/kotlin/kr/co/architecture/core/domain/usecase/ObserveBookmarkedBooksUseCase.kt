package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.domain.entity.Book

interface ObserveBookmarkedBooksUseCase {
  operator fun invoke(): Flow<List<Book>>
}