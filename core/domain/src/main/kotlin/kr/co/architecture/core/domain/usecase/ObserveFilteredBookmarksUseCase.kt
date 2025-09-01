package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.enums.SortDirectionEnum
import kr.co.architecture.core.domain.enums.SortPriceRangeEnum

interface ObserveFilteredBookmarksUseCase {

  operator fun invoke(filters: Flow<BookmarkFilter>): Flow<List<Book>>

  data class BookmarkFilter(
    val query: String = "",
    val sortDirection: SortDirectionEnum = SortDirectionEnum.ASCENDING,
    val priceRange: SortPriceRangeEnum = SortPriceRangeEnum.ALL,
    val threshold: Int = 10_000
  )
}