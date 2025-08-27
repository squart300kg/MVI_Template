package kr.co.architecture.core.domain.entity

import java.util.Date

data class SearchedBook(
  val pageable: Pageable,
  val books: List<Book>
)

data class Book(
  val title: String,
  val authors: List<String>,
  val publisher: String,
  val dateTime: Date,
  val price: Price,
  val url: String,
  val thumbnail: String
)

sealed interface Price {
  @JvmInline
  value class SalePrice(val value: Int) : Price
  @JvmInline
  value class DiscountPrice(val value: Int) : Price
}