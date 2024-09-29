package kr.co.architecture.repository

import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getList(): Flow<List<ArticleDto>>

}