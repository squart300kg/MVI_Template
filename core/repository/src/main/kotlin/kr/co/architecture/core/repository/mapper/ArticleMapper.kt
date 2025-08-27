package kr.co.architecture.core.repository.mapper

import kr.co.architecture.core.domain.entity.Article
import kr.co.architecture.core.network.model.ArticleResponse

object ArticleMapper {
  fun mapperToDomainResponse(apiResponse: List<ArticleResponse>) =
    apiResponse.map { Article(it.title) }
}