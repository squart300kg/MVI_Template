package kr.co.architecture.yeo.feature.search.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kr.co.architecture.yeo.core.domain.entity.Book
import kr.co.architecture.yeo.core.domain.usecase.ObserveBookmarkedBooksUseCase

class FakeObserveBookmarkedBooksUseCase : ObserveBookmarkedBooksUseCase {
  private val bookFlow = MutableSharedFlow<List<Book>>(extraBufferCapacity = 16)
  override fun invoke(): Flow<List<Book>> = bookFlow
  suspend fun emit(list: List<Book>) = bookFlow.emit(list)
}