package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.enums.SearchTypeEnum
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  suspend operator fun invoke(params: Params): Flow<SearchedBook> {
    return combine(
      flow = flowOf(bookRepository.searchBook(params)),
      flow2 = bookRepository.observeBookmarkedBooks()
    ) { searchedBooks, bookmarkedBooks ->
      searchedBooks.copy(
        books = searchedBooks.books.map { book ->
          book.copy(
            isBookmarked = bookmarkedBooks.find { it.isbn == book.isbn } != null
          )
        }
      )
    }
  }

  data class Params(
    val page: Int,
    val query: String,
    val sortTypeEnum: SortTypeEnum,
    val searchTypeEnum: SearchTypeEnum
  )
}