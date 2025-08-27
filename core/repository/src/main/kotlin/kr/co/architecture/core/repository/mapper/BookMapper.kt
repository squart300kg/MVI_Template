package kr.co.architecture.core.repository.mapper

import kr.co.architecture.core.database.entity.BookEntity
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.Pageable
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.repository.enums.SortTypeDtoEnum

object BookMapper {
  fun mapperToDomain(book: BookEntity) =
    Book(
      isbn = book.isbn,
      title = book.title,
      authors = book.authors,
      publisher = book.publisher,
      dateTime = book.dateTime,
      price = book.price,
      url = book.url,
      thumbnail = book.thumbnail,
      isBookmarked = false
    )

  fun mapperToEntity(book: Book) =
    BookEntity(
      isbn = book.isbn,
      title = book.title,
      authors = book.authors,
      publisher = book.publisher,
      dateTime = book.dateTime,
      price = book.price,
      url = book.url,
      thumbnail = book.thumbnail
    )
}