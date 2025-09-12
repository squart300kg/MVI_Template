package kr.co.architecture.yeo.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.yeo.core.domain.entity.Book

/**
 * 즐겨찾기 목록을 그대로 관찰하는 유스케이스.
 * 데이터가 바뀌면 새로운 리스트를 방출합니다.
 */
interface ObserveBookmarkedBooksUseCase {

  /**
   * @return 즐겨찾기 도서 리스트 스트림
   */
  operator fun invoke(): Flow<List<Book>>
}