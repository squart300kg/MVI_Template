package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.enums.SortEnum

/**
 * 검색 리스트 유스케이스.
 * 질의/정렬/페이지를 기반으로 도서 목록을 반환합니다.
 */
interface SearchBooksUseCase {

  /**
   * 실행 연산자.
   *
   * @param params 검색 파라미터
   * @return 검색 결과(페이지 정보 포함)
   */
  suspend operator fun invoke(params: Params): SearchedBooks

  /**
   * 검색 입력 파라미터.
   *
   * @property page 페이지(1부터)
   * @property query 질의어
   * @property sortEnum 정렬 옵션(기본: 정확도)
   */
  data class Params(
    val page: Int,
    val query: String,
    val sortEnum: SortEnum = SortEnum.ACCURACY
  )
}