package kr.co.architecture.core.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.SearchBookUseCase

/**
 * 도서 데이터의 도메인 레포지토리 경계.
 *
 * - 원격/로컬/캐시 여부는 구현체가 결정
 * - UI/UseCase는 이 인터페이스만 의존
 */
interface BookRepository {

  /**
   * 즐겨찾기된 도서를 관찰합니다.
   * 데이터 변경 시 새로운 리스트를 방출합니다.
   *
   * @return 즐겨찾기 도서의 지속적인 [Flow]
   */
  fun observeBookmarkedBooks(): Flow<List<Book>>

  /**
   * 즐겨찾기 저장/삭제를 수행합니다.
   *
   * @param params 토글 파라미터(ISBN, 저장/삭제)
   */
  suspend fun toggleBookmark(params: ToggleBookmarkUseCase.Params)

  /**
   * 질의/정렬/페이지 기반 도서 검색을 수행합니다.
   *
   * @param params 검색 파라미터
   * @return 검색 결과(페이지 정보 포함)
   */
  suspend fun searchBooks(params: SearchBooksUseCase.Params): SearchedBooks

  /**
   * 단일 도서(ISBN)를 조회합니다.
   * 존재하지 않으면 `null`.
   *
   * @param params 조회 파라미터(ISBN)
   */
  suspend fun searchBook(params: SearchBookUseCase.Params): Book?
}