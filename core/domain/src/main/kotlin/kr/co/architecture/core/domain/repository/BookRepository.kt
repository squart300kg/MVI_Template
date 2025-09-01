package kr.co.architecture.core.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.SearchBookUseCase

interface BookRepository {

  fun observeBookmarkedBooks(): Flow<List<Book>>

  suspend fun toggleBookmark(params: ToggleBookmarkUseCase.Params)

  suspend fun searchBooks(params: SearchBooksUseCase.Params): SearchedBooks

  suspend fun searchBook(params: SearchBookUseCase.Params): Book?

}