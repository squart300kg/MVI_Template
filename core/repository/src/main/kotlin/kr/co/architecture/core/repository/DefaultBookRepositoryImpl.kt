package kr.co.architecture.core.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kr.co.architecture.core.database.dao.BookSearchDao
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.safeGet
import kr.co.architecture.core.repository.mapper.SearchedBookMapper
import kr.co.architecture.core.repository.mapper.BookMapper
import javax.inject.Inject

class DefaultBookRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi,
  private val bookSearchDao: BookSearchDao
) : BookRepository {

  private data class QueryKey(val query: String, val sort: SortTypeEnum)
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

    when (params.bookmarkToggleTypeEnum) {
      BookmarkToggleTypeEnum.SAVE -> {
        bookSearchDao.upsert(BookMapper.mapperToEntity(cachedBook))
        // 캐시 동기화
        cacheMutex.withLock {
          cachedSearchedBooks[ISBN(cachedBook.isbn)] = cachedBook.copy(isBookmarked = true)
        }
      }
      BookmarkToggleTypeEnum.DELETE -> {
        bookSearchDao.delete(cachedBook.isbn)
        // 캐시 동기화
        cacheMutex.withLock {
          cachedSearchedBooks[ISBN(cachedBook.isbn)] = cachedBook.copy(isBookmarked = false)
        }
      }
    }
  }

  override suspend fun searchBooks(params: SearchBooksUseCase.Params): SearchedBooks {
    val newKey = QueryKey(params.query, params.sortTypeEnum)

    // 다른 쿼리/정렬로 시작하거나 첫 페이지면 캐시 초기화
    if (currentKey != newKey || params.page == 1) {
      cachedSearchedBooks.clear()
      currentKey = newKey
    }

    return remoteApi.searchBook(
      query = params.query,
      sort = SearchedBookMapper.mapperToDto(params.sortTypeEnum).value,
      page = params.page
    )
      .safeGet()
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

