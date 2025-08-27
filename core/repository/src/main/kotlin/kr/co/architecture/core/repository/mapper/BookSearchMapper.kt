package kr.co.architecture.core.repository.mapper

import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.Pageable
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.network.model.SearchedBookApiResponse
import kr.co.architecture.core.repository.enums.SortTypeDtoEnum

object BookSearchMapper {
  fun mapperToDomain(apiResponse: SearchedBookApiResponse) =
    SearchedBook(
      pageable = Pageable(
        isEnd = apiResponse.meta.isEnd,
        pageableCount = apiResponse.meta.pageableCount
      ),
      books = apiResponse.documents.map {
        Book(
          title = it.title,
          authors = it.authors,
          publisher = it.publisher,
          dateTime = it.dateTime,
          price =
            if (it.salePrice == 0) Price.DiscountPrice(it.price)
            else Price.SalePrice(it.salePrice),
          url = it.url,
          thumbnail = it.thumbnail,
          isBookmarked = false
        )
      }
    )

  fun mapperToDto(domainRequest: SortTypeEnum) = when (domainRequest) {
    SortTypeEnum.ACCURACY -> SortTypeDtoEnum.ACCURACY
    SortTypeEnum.RECENCY -> SortTypeDtoEnum.RECENCY
  }
}