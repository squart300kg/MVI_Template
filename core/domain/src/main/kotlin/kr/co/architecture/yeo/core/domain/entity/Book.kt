package kr.co.architecture.yeo.core.domain.entity

data class Book(
  val isbn: String,
  val title: String,
  val authors: List<String>,
  val publisher: String,
  val dateTime: String,
  val price: Price,
  val url: String,
  val thumbnail: String,
  val contents: String,
  val isBookmarked: Boolean,
)