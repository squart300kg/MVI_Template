package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum

/**
 * 즐겨찾기 저장/삭제 유스케이스.
 * 구현체는 레포지토리를 통해 반영합니다.
 */
interface ToggleBookmarkUseCase {

  /**
   * 실행 연산자.
   *
   * @param params 북마크 토글 파라미터
   */
  suspend operator fun invoke(params: Params)

  /**
   * 토글 입력 파라미터.
   *
   * @property bookmarkToggleTypeEnum 저장/삭제 구분
   * @property isbn 대상 도서 ISBN
   */
  data class Params(
    val bookmarkToggleTypeEnum: BookmarkToggleTypeEnum,
    val isbn: ISBN
  )
}