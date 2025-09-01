package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.enums.SortDirectionEnum
import kr.co.architecture.core.domain.enums.SortPriceRangeEnum

/**
 * 실시간 필터(검색어/정렬/가격)를 적용해 방출하는 유스케이스.
 *
 * 외부에서 전달되는 필터 흐름([filters])이 변경되거나
 * 내부 즐겨찾기 데이터가 갱신되면, 정제된 리스트를 새로 방출합니다.
 */
interface ObserveFilteredBookmarksUseCase {

  /**
   * @param filters 검색/정렬/가격 조건 스트림
   * @return 필터가 적용된 즐겨찾기 도서 리스트 스트림
   */
  operator fun invoke(filters: Flow<BookmarkFilter>): Flow<List<Book>>

  /**
   * @property query 제목/출판사/저자 대상 부분 일치(대소문자 무시) 검색어
   * @property sortDirection 제목 정렬 방향(오름/내림)
   * @property priceRange 가격 필터 구간(ALL/LESS/MORE)
   * @property threshold 가격 경계값(기본 10,000원)
   */
  data class BookmarkFilter(
    val query: String = "",
    val sortDirection: SortDirectionEnum = SortDirectionEnum.ASCENDING,
    val priceRange: SortPriceRangeEnum = SortPriceRangeEnum.ALL,
    val threshold: Int = 10_000
  )
}