package kr.co.architecture.core.domain.entity

data class SearchedBooks(
  val pageable: Pageable,
  val books: List<Book>
)