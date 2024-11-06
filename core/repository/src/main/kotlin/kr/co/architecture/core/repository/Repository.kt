package kr.co.architecture.core.repository

import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getList(): Flow<List<ArticleDto>>

}