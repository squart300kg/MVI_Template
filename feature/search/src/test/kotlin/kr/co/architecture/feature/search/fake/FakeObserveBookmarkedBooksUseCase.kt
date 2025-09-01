package kr.co.architecture.feature.search.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.usecase.ObserveBookmarkedBooksUseCase

class FakeObserveBookmarkedBooksUseCase : ObserveBookmarkedBooksUseCase {
  private val upstream = MutableSharedFlow<List<Book>>(extraBufferCapacity = 16)
  override fun invoke(): Flow<List<Book>> = upstream
  suspend fun emit(list: List<Book>) = upstream.emit(list)
}