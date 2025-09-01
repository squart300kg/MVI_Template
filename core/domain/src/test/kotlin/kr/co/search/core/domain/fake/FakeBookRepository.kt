package kr.co.search.core.domain.fake

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.repository.BookRepository
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase

class FakeBookRepository : BookRepository {
    val books = MutableStateFlow<List<Book>>(emptyList())

    override fun observeBookmarkedBooks(): StateFlow<List<Book>> = books

    override suspend fun toggleBookmark(params: ToggleBookmarkUseCase.Params) {
      // not used in this test
    }

    override suspend fun searchBooks(params: SearchBooksUseCase.Params): SearchedBooks {
      throw UnsupportedOperationException("Not used in this test")
    }

    override suspend fun searchBook(params: SearchBookUseCase.Params) = null
  }