package kr.co.architecture.core.repository

import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.ArticleDto
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : Repository {

  override suspend fun getList(): List<ArticleDto> {
    return remoteApi.getList()
      .getOrThrowAppFailure()
      .let(ArticleDto::mapperToDto)
  }
}


