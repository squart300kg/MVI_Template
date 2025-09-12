package kr.co.architecture.yeo.core.domain.entity

data class SearchedBooks(
  val pageable: Pageable,
  val books: List<Book>
)