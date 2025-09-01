package kr.co.architecture.feature.search.fake

import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.Pageable
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBooks


fun makeBook(
  i: Int,
  bookmarked: Boolean = false
): Book = Book(
  isbn = "isbn$i",
  title = "제목 $i",
  authors = listOf("저자 $i"),
  publisher = "출판사 $i",
  dateTime = "2021-01-${"%02d".format(i)}T00:00:00.000+09:00", // String 기반 가정
  price = Price.Origin(origin = i * 1000),
  url = "https://example.com/$i",
  thumbnail = "",
  contents = "설명 $i",
  isBookmarked = bookmarked
)

fun searchedBooks(count: Int, end: Boolean = false) = SearchedBooks(
  books = (1..count).map { makeBook(it) },
  pageable = Pageable(isEnd = end)
)