package kr.co.architecture.core.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.usecase.SearchBookUseCaseImpl
import kr.co.architecture.core.domain.usecase.SearchBooksUseCaseImpl
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCaseImpl

interface BookRepository {

  fun observeBookmarkedBooks(): Flow<List<Book>>

  suspend fun toggleBookmark(params: ToggleBookmarkUseCaseImpl.Params)

  suspend fun searchBooks(params: SearchBooksUseCaseImpl.Params): SearchedBooks

  suspend fun searchBook(params: SearchBookUseCaseImpl.Params): Book?

}