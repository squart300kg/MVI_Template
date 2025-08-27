package kr.co.architecture.core.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase

interface BookRepository {

  fun observeBookmarkedBooks(): Flow<List<Book>>

  suspend fun toggleBookmark(params: ToggleBookmarkUseCase.Params)

  suspend fun searchBook(params: SearchBookUseCase.Params): SearchedBook

}