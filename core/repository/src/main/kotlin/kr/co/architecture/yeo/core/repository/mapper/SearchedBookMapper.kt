package kr.co.architecture.yeo.core.repository.mapper

import kr.co.architecture.yeo.core.domain.entity.Pageable
import kr.co.architecture.yeo.core.domain.entity.SearchedBooks
import kr.co.architecture.yeo.core.domain.enums.SortEnum
import kr.co.architecture.yeo.core.network.model.SearchedBookApiResponse
import kr.co.architecture.yeo.core.repository.enums.SortDtoEnum

object SearchedBookMapper {
  fun mapperToDomain(apiResponse: SearchedBookApiResponse) =
    SearchedBooks(
      pageable = Pageable(
        isEnd = apiResponse.meta.isEnd
      ),
      books = apiResponse.documents.map(BookMapper::mapperToDomain)
    )

  fun mapperToDto(domainRequest: SortEnum) = when (domainRequest) {
    SortEnum.ACCURACY -> SortDtoEnum.ACCURACY
    SortEnum.LATEST -> SortDtoEnum.LATEST
  }
}