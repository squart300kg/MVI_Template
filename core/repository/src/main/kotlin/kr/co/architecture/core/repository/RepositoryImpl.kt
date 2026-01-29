package kr.co.architecture.core.repository

import kr.co.architecture.core.domain.repository.ArticleRepository
import kr.co.architecture.core.model.Article
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : ArticleRepository {

  override suspend fun getList(): List<Article> {
    return remoteApi.getList()
      .getOrThrowAppFailure()
      .map { Article(name = it.title) }
  }
}
