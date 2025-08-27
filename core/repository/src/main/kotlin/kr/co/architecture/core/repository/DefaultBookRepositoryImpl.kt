package kr.co.architecture.core.repository

import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.repository.BookRepository
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.safeGet
import kr.co.architecture.core.repository.mapper.BookSearchMapper
import javax.inject.Inject

class DefaultBookRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : BookRepository {

  override suspend fun searchBook(params: SearchBookUseCase.Params): SearchedBook {
    return remoteApi.searchBook(
      query = params.query,
      sort = BookSearchMapper.mapperToDto(params.sortTypeEnum).value,
      page = params.page
    )
      .safeGet()
      .let(BookSearchMapper::mapperToDomain)
  }
}
