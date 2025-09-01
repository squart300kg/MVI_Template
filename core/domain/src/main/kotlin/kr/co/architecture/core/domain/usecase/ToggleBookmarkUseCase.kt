package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum

/**
 * 즐겨찾기 저장/삭제 유스케이스.
 */
interface ToggleBookmarkUseCase {

  /**
   * @param params 북마크 토글 파라미터
   */
  suspend operator fun invoke(params: Params)

  /**
   * @property bookmarkToggleTypeEnum 저장/삭제 구분
   * @property isbn 대상 도서 ISBN
   */
  data class Params(
    val bookmarkToggleTypeEnum: BookmarkToggleTypeEnum,
    val isbn: ISBN
  )
}