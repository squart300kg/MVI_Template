package kr.co.architecture.core.repository.mapper

import kr.co.architecture.core.database.entity.BookEntity
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.Price

object BookMapper {
  fun mapperToDomain(book: BookEntity) =
    Book(
      isbn = book.isbn,
      title = book.title,
      authors = book.authors.split(", "),
      publisher = book.publisher,
      dateTime = book.dateTime,
      price = when (book.discountedPrice != 0) {
        true -> Price.Discount(
          origin = book.originPrice,
          discounted = book.discountedPrice)
        false -> Price.Origin(book.originPrice)
      },
      url = book.url,
      thumbnail = book.thumbnail,
      isBookmarked = true
    )

  fun mapperToEntity(book: Book) =
    BookEntity(
      isbn = book.isbn,
      title = book.title,
      authors = book.authors.joinToString(", "),
      publisher = book.publisher,
      dateTime = book.dateTime,
      discountedPrice =
        if (book.price is Price.Discount) (book.price as Price.Discount).discounted
        else 0,
      originPrice = book.price.origin,
      url = book.url,
      thumbnail = book.thumbnail
    )
}