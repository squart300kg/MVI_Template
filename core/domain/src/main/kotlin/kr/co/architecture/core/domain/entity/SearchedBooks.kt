package kr.co.architecture.core.domain.entity

import java.util.Date

// TODO: 모델 패키지 정리

data class SearchedBooks(
  val pageable: Pageable,
  val books: List<Book>
)

data class Book(
  val isbn: String,
  val title: String,
  val authors: List<String>,
  val publisher: String,
  val dateTime: Date,
  val price: Price,
  val url: String,
  val thumbnail: String,
  val contents: String,
  val isBookmarked: Boolean,
)

@JvmInline
value class ISBN(val value: String)

sealed interface Price {
  val origin: Int
  @JvmInline
  value class Origin(override val origin: Int) : Price
  data class Discount(override val origin: Int, val discounted: Int) : Price
}