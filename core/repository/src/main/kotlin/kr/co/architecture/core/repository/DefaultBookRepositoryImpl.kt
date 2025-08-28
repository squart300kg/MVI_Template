package kr.co.architecture.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kr.co.architecture.core.database.dao.BookSearchDao
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.safeGet
import kr.co.architecture.core.repository.mapper.SearchedBookMapper
import kr.co.architecture.core.repository.mapper.BookMapper
import java.util.LinkedList
import javax.inject.Inject

class DefaultBookRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi,
  private val bookSearchDao: BookSearchDao
) : BookRepository {

  private val cachedSearchedBooks = LinkedList<Book>()

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
      }
      BookmarkToggleTypeEnum.DELETE -> {
        bookSearchDao.delete(cachedBook.isbn)
      }
    }
  }

  override suspend fun searchBooks(params: SearchBooksUseCase.Params): SearchedBooks {
    return remoteApi.searchBook(
      query = params.query,
      sort = SearchedBookMapper.mapperToDto(params.sortTypeEnum).value,
      page = params.page
    )
      .safeGet()
      .also {
        // TODO: 주석 지우기
        println("apiLog, meta ; ${it.meta}")
        it.documents.forEachIndexed { index, item ->
          println("apiLog, document ; $index : $item")
        }
      }
      .let(SearchedBookMapper::mapperToDomain)
      .also { cachedSearchedBooks.addAll(it.books) }
  }

  override suspend fun searchBook(params: SearchBookUseCase.Params): Book? {
    val isbn = params.isbn.value
    val localBooks = observeBookmarkedBooks().first()
    return cachedSearchedBooks
      .firstOrNull { it.isbn == isbn }
      ?.let { remoteBook ->
        val isBookmarked = localBooks.any { it.isbn == remoteBook.isbn }
        if (isBookmarked) remoteBook.copy(isBookmarked = true)
        else remoteBook
      } ?: run {
      localBooks.find { it.isbn == isbn }
    }
  }
}
