package kr.co.architecture.core.repository

import kr.co.architecture.core.domain.entity.Article
import kr.co.architecture.core.domain.repository.Repository
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.safeGet
import kr.co.architecture.core.repository.mapper.ArticleMapper
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : Repository {

  override suspend fun getList(): List<Article> {
    return remoteApi.getList()
      .safeGet()
      .let(ArticleMapper::mapperToDomainResponse)
  }
}


