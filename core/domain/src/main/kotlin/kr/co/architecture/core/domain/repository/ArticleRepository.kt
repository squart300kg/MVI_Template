package kr.co.architecture.core.domain.repository

import kr.co.architecture.core.model.Article

interface ArticleRepository {
  suspend fun getList(): List<Article>
}
