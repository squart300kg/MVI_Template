package kr.co.architecture.yeo.core.domain.usecase

import kr.co.architecture.yeo.core.domain.entity.Book
import kr.co.architecture.yeo.core.domain.entity.ISBN

/**
 * 단일 도서 조회 유스케이스(ISBN 기준).
 */
interface SearchBookUseCase {

  /**
   * @param params 조회 파라미터(ISBN)
   * @return 도서 or `null`
   */
  suspend operator fun invoke(params: Params): Book?

  /**
   * @property isbn 대상 도서 ISBN
   */
  data class Params(
    val isbn: ISBN
  )
}