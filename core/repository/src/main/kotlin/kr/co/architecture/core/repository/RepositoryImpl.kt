package kr.co.architecture.core.repository

import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.repository.Repository
import kr.co.architecture.core.domain.usecase.GetListUseCase
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.safeGet
import kr.co.architecture.core.repository.mapper.BookSearchMapper
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : Repository {

  override suspend fun searchBook(params: GetListUseCase.Params): SearchedBook {
    return remoteApi.searchBook(
      query = params.query,
      sort = BookSearchMapper.mapperToDto(params.sortTypeEnum).value,
      page = params.page
    )
      .safeGet()
      .let(BookSearchMapper::mapperToDomain)
  }
}
