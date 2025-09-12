package kr.co.architecture.yeo.test.testing.ui

object SearchTags {
  const val HEADER_TEXT_FIELD = "search:text"
  const val HEADER_CLEAR = "search:clear"
  const val SORT_CHIP = "search:sortChip"
  const val RESULT_LIST = "search:list"
  fun sortItem(name: String) = "search:sort:$name"
  fun bookCard(isbn: String) = "book:$isbn"
  fun bookmark(isbn: String) = "bookmark:$isbn"
}