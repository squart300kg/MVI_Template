package kr.co.architecture.core.repository

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.repository.dto.ArticleDto

interface Repository {

  fun getList(): Flow<List<ArticleDto>>

}