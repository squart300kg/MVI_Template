package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  suspend operator fun invoke(params: Params) {

  }

  data class Params(
    val bookmarkToggleTypeEnum: BookmarkToggleTypeEnum
  )
}