package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.architecture.core.domain.di.IoDispatcher
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
  private val bookRepository: BookRepository,
  @IoDispatcher private val  dispatcher: CoroutineDispatcher
) {
  suspend operator fun invoke(params: Params) {
    withContext(dispatcher) {
      bookRepository.toggleBookmark(params)
    }
  }

  data class Params(
    val bookmarkToggleTypeEnum: BookmarkToggleTypeEnum,
    val isbn: ISBN
  )
}