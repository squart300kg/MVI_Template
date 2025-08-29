package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.BookmarkFilter
import kr.co.architecture.core.domain.entity.matches
import kr.co.architecture.core.domain.enums.sortedByTitle
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class ObserveFilteredBookmarksUseCase @Inject constructor(
  private val repository: BookRepository
) {
  operator fun invoke(filters: Flow<BookmarkFilter>): Flow<List<Book>> =
    combine(
      flow = repository.observeBookmarkedBooks(),
      flow2 = filters.distinctUntilChanged()
    ) { books, filter -> books.applyFilter(filter) }
      .distinctUntilChanged()

  private fun List<Book>.applyFilter(filter: BookmarkFilter): List<Book> {
    // 1) 제목/출판사/저자 기준, 필터링 및 정렬
    val filteredBooksByQuery =
      if (filter.query.isBlank()) this
      else {
        val query = filter.query.trim().lowercase()
        this@applyFilter.filter { book ->
          book.title.lowercase().contains(query) ||
            book.publisher.lowercase().contains(query) ||
            book.authors.any { it.lowercase().contains(query) }
        }
    }

    // 2) 가격 필터링 및 정렬
    val filteredBookByPrice = filteredBooksByQuery
      .filter { it.price.matches(filter.priceRange, filter.threshold) }

    // 3) 제목 기준, 오름/내림차순 정렬
    return filteredBookByPrice.sortedByTitle(filter.sortDirection)
  }
}