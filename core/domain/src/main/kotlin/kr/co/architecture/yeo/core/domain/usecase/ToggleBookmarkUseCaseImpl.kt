package kr.co.architecture.yeo.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.architecture.yeo.core.domain.di.IoDispatcher
import kr.co.architecture.yeo.core.domain.repository.BookRepository
import kr.co.architecture.yeo.core.domain.usecase.ToggleBookmarkUseCase.Params
import javax.inject.Inject

class ToggleBookmarkUseCaseImpl @Inject constructor(
  private val bookRepository: BookRepository,
  @IoDispatcher private val  dispatcher: CoroutineDispatcher
): ToggleBookmarkUseCase {
  override suspend operator fun invoke(params: Params) {
    withContext(dispatcher) {
      bookRepository.toggleBookmark(params)
    }
  }
}