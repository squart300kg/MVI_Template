package kr.co.architecture.core.domain.repository

import kr.co.architecture.core.domain.entity.Article

interface Repository {

  suspend fun getList(): List<Article>

}