package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  suspend operator fun invoke(params: Params) {
    bookRepository.toggleBookmark(params)
  }

  data class Params(
    val bookmarkToggleTypeEnum: BookmarkToggleTypeEnum,
    val isbn: ISBN
  )
}