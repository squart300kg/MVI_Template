package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.ArticleDto

interface Repository {

  suspend fun getList(): List<ArticleDto>

}