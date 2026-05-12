package kr.co.architecture.core.repository

import kr.co.architecture.core.domain.repository.ArticleRepository
import kr.co.architecture.core.model.Article
import kr.co.architecture.core.network.BuildConfig
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : ArticleRepository {

  override suspend fun getList(): List<Article> {
    if (BuildConfig.apiKey.isBlank()) {
      return listOf(
        Article(name = "Clean architecture template"),
        Article(name = "MVI feature module"),
        Article(name = "Typed navigation")
      )
    }

    return remoteApi.getList()
      .getOrThrowAppFailure()
      .map { Article(name = it.title) }
  }
}
