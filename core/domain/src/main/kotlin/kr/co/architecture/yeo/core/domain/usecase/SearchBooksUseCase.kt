package kr.co.architecture.yeo.core.domain.usecase

import kr.co.architecture.yeo.core.domain.entity.SearchedBooks
import kr.co.architecture.yeo.core.domain.enums.SortEnum

/**
 * 질의/정렬/페이지를 기반으로 도서 목록을 반환하는 오스케이스
 */
interface SearchBooksUseCase {

  /**
   * @param params 검색 파라미터
   * @return 검색 결과(페이지 정보 포함)
   */
  suspend operator fun invoke(params: Params): SearchedBooks

  /**
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