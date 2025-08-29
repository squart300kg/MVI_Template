package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.BookmarkFilter
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.enums.SortDirectionEnum
import kr.co.architecture.core.domain.enums.SortPriceRangeEnum
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

    val filteredBooksByPrice = filter.priceRange
      ?.let { range ->
        when (range) {
          SortPriceRangeEnum.LESS -> filteredBooksByQuery.filter { it.price.toAmount() <  filter.threshold }
          SortPriceRangeEnum.MORE -> filteredBooksByQuery.filter { it.price.toAmount() >= filter.threshold }
        }
      } ?: filteredBooksByQuery

    return when (filter.sortDirection) {
      SortDirectionEnum.ASCENDING  -> filteredBooksByPrice.sortedBy { it.title }
      SortDirectionEnum.DESCENDING -> filteredBooksByPrice.sortedByDescending { it.title }
    }
  }

  private fun Price.toAmount(): Int =
    when (this) {
      is Price.Discount -> discounted
      is Price.Origin -> origin
    }
}