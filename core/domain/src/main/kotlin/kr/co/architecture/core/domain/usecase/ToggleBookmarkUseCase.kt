package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum

interface ToggleBookmarkUseCase {
  suspend operator fun invoke(params: Params)

  data class Params(
    val bookmarkToggleTypeEnum: BookmarkToggleTypeEnum,
    val isbn: ISBN
  )
}