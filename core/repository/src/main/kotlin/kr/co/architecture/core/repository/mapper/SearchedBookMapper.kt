package kr.co.architecture.core.repository.mapper

import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.Pageable
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.network.model.NO_EXIST_PRICE
import kr.co.architecture.core.network.model.SearchedBookApiResponse
import kr.co.architecture.core.repository.enums.SortTypeDtoEnum

object SearchedBookMapper {
  fun mapperToDomain(apiResponse: SearchedBookApiResponse) =
    SearchedBooks(
      pageable = Pageable(
        isEnd = apiResponse.meta.isEnd,
        pageableCount = apiResponse.meta.pageableCount
      ),
      books = apiResponse.documents.map {
        Book(
          isbn = it.isbn,
          title = it.title,
          authors = it.authors,
          publisher = it.publisher,
          dateTime = it.dateTime,
          price = when (it.salePrice != NO_EXIST_PRICE) {
            true -> Price.Discount(
              origin = it.price,
              discounted = it.salePrice
            )
            false -> Price.Origin(it.price)
          },
          url = it.url,
          thumbnail = it.thumbnail,
          contents = it.contents,
          isBookmarked = false
        )
      }
    )

  fun mapperToDto(domainRequest: SortTypeEnum) = when (domainRequest) {
    SortTypeEnum.ACCURACY -> SortTypeDtoEnum.ACCURACY
    SortTypeEnum.RECENCY -> SortTypeDtoEnum.RECENCY
  }
}