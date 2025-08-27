package kr.co.architecture.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.co.architecture.core.database.dao.BookSearchDao
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
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

  private val cachedSearchedBooks = mutableListOf<Book>()

  override fun observeBookmarkedBooks(): Flow<List<Book>> =
    bookSearchDao.observeBookmarkedBooks()
      .map { it.map(BookMapper::mapperToDomain) }

  override suspend fun toggleBookmark(params: ToggleBookmarkUseCase.Params) {
    val cachedBook = cachedSearchedBooks.find { it.isbn == params.isbn.value }
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

  override suspend fun searchBook(params: SearchBookUseCase.Params): SearchedBook {
    return remoteApi.searchBook(
      query = params.query,
      sort = SearchedBookMapper.mapperToDto(params.sortTypeEnum).value,
      page = params.page
    )
      .safeGet()
      .let(SearchedBookMapper::mapperToDomain)
      .also { cachedSearchedBooks.addAll(it.books) }
  }
}
