package kr.co.architecture.yeo.core.repository.mapper

import kr.co.architecture.yeo.core.database.entity.BookEntity
import kr.co.architecture.yeo.core.domain.entity.Book
import kr.co.architecture.yeo.core.domain.entity.Price
import kr.co.architecture.yeo.core.network.model.NO_EXIST_PRICE
import kr.co.architecture.yeo.core.network.model.SearchedBookApiResponse

object BookMapper {
  fun mapperToDomain(book: SearchedBookApiResponse.Book) =
    Book(
      isbn = book.isbn,
      title = book.title,
      authors = book.authors,
      publisher = book.publisher,
      dateTime = book.dateTime,
      price = when (book.salePrice != NO_EXIST_PRICE) {
        true -> Price.Discount(
          origin = book.price,
          discounted = book.salePrice
        )
        false -> Price.Origin(book.price)
      },
      url = book.url,
      thumbnail = book.thumbnail,
      contents = book.contents,
      isBookmarked = false
    )

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
      contents = book.contents,
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
      contents = book.contents,
      thumbnail = book.thumbnail
    )
}