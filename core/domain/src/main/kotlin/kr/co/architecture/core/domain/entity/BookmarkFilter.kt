package kr.co.architecture.core.domain.entity

import kr.co.architecture.core.domain.enums.SortDirectionEnum
import kr.co.architecture.core.domain.enums.SortPriceRangeEnum

data class BookmarkFilter(
  val query: String = "",
  val sortDirection: SortDirectionEnum = SortDirectionEnum.ASCENDING,
  val priceRange: SortPriceRangeEnum = SortPriceRangeEnum.ALL,
  val threshold: Int = 10_000
)