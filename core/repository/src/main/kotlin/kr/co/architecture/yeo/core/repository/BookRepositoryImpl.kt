package kr.co.architecture.yeo.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kr.co.architecture.yeo.core.database.dao.BookSearchDao
import kr.co.architecture.yeo.core.domain.entity.Book
import kr.co.architecture.yeo.core.domain.entity.ISBN
import kr.co.architecture.yeo.core.domain.entity.SearchedBooks
import kr.co.architecture.yeo.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.yeo.core.domain.enums.SortEnum
import kr.co.architecture.yeo.core.domain.repository.BookRepository
import kr.co.architecture.yeo.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.yeo.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.yeo.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.yeo.core.network.RemoteApi
import kr.co.architecture.yeo.core.repository.mapper.BookMapper
import kr.co.architecture.yeo.core.repository.mapper.SearchedBookMapper
import kr.co.architecture.yeo.core.repository.mapper.getOrThrowDomainFailure
import javax.inject.Inject

// TODO: 원래 구조?
//  1. core:repository에 `Local/Remote DataSource`인터페이스 정의
//  2. core:dataSource에 retrofit/room 구현체 정의 및 인터페이스 타입 반환하는 DI객체 생성
//  3. core:repository에서 data source 구현 객체 주입 및 사용

/**
 * Clean Architecture구조 상, RemoteDataSource / LocalDataSource를 두어야함이 맞으나, 아래 이유로 생략하였습니다.
 *
 * 1. 앱 크기
 * 2. DataSource에 정의되는 mapper가 동일 모델을 파싱하는 용도에 지나지 않음.
 *
 * 따라서 `core:network`의 retrofit의 의존성을 `implementation()`이 아닌, `api()`를 통해, `core:repository`로 전파합니다.
 */
class BookRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi,
  private val bookSearchDao: BookSearchDao
) : BookRepository {

  private data class QueryKey(val query: String, val sort: SortEnum)
  private var currentKey: QueryKey? = null

  // DefaultBookRepositoryImpl은 Singleton이어서, 공유자원 관리 및 RaceCondition
  // 방지가 필요하여 Mutex 선언
  private val cacheMutex = Mutex()
  private val cachedSearchedBooks = LinkedHashMap<ISBN, Book>()

  override fun observeBookmarkedBooks(): Flow<List<Book>> =
    bookSearchDao.observeBookmarkedBooks()
      .map { it.map(BookMapper::mapperToDomain) }

  override suspend fun toggleBookmark(params: ToggleBookmarkUseCase.Params) {
    val cachedBook = searchBook(SearchBookUseCase.Params(params.isbn))
    checkNotNull(cachedBook) {
      "cached UI `Book` and Domain `Book` does not sync"
    }

    // TODO: 개선
    //  1. .upsert, delete를 캐시 동기화 이후에 하기
    //  2. mutex범위를 전체로 걸기
    cacheMutex.withLock {
      when (params.bookmarkToggleTypeEnum) {
        BookmarkToggleTypeEnum.SAVE -> {
          // 캐시 동기화
//        cacheMutex.withLock {
          cachedSearchedBooks[ISBN(cachedBook.isbn)] = cachedBook.copy(isBookmarked = true)
          bookSearchDao.upsert(BookMapper.mapperToEntity(cachedBook))
//        }
        }
        BookmarkToggleTypeEnum.DELETE -> {
          // 캐시 동기화
//        cacheMutex.withLock {
          cachedSearchedBooks[ISBN(cachedBook.isbn)] = cachedBook.copy(isBookmarked = false)
          bookSearchDao.delete(cachedBook.isbn)
//        }
        }
      }
    }
  }

  override suspend fun searchBooks(params: SearchBooksUseCase.Params): SearchedBooks {
    val newKey = QueryKey(params.query, params.sortEnum)

    // 다른 쿼리/정렬로 시작하거나 첫 페이지면 캐시 초기화
    if (currentKey != newKey || params.page == 1) {
      cachedSearchedBooks.clear()
      currentKey = newKey
    }

    return remoteApi.searchBook(
      query = params.query,
      sort = SearchedBookMapper.mapperToDto(params.sortEnum).value,
      page = params.page
    )
      .getOrThrowDomainFailure()
      .let(SearchedBookMapper::mapperToDomain)
      .also { searchedBooks ->
        cacheMutex.withLock {
          searchedBooks.books.forEach { book ->
            cachedSearchedBooks[ISBN(book.isbn)] = book
          }
        }
      }
  }

  override suspend fun searchBook(params: SearchBookUseCase.Params): Book? {
    val isbn = params.isbn
    val localBooks = observeBookmarkedBooks().first()
    return cachedSearchedBooks[isbn]
      ?.let { cachedBook ->
        val isBookmarked = localBooks.any { it.isbn == cachedBook.isbn }
        if (isBookmarked) cachedBook.copy(isBookmarked = true)
        else cachedBook
      } ?: run { localBooks.find { it.isbn == isbn.value } }
      ?.also { cachedSearchedBooks[isbn] = it }
  }
}

