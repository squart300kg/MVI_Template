package kr.co.architecture.test.testing.ui

object SearchTags {
  const val HeaderTextField = "search:text"
  const val HeaderClear = "search:clear"
  const val SortChip = "search:sortChip"
  fun sortItem(name: String) = "search:sort:$name"
  const val ResultsList = "search:list"
  fun bookCard(isbn: String) = "book:$isbn"
  fun bookmark(isbn: String) = "bookmark:$isbn"
}