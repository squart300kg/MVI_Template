package kr.co.architecture.core.domain.entity

import java.util.Date

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