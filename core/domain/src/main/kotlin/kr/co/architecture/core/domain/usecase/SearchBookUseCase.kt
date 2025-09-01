package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.ISBN

interface SearchBookUseCase {
  suspend operator fun invoke(params: Params): Book?

  data class Params(
    val isbn: ISBN
  )
}