package kr.co.architecture.core.repository

import com.skydoves.sandwich.suspendOperator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.model.ArticleResponse
import kr.co.architecture.core.network.model.CommonResponse
import kr.co.architecture.core.network.operator.ResponseBaseOperator
import javax.inject.Inject

data class ArticleDto(
    val name: String
)

fun mapperToDto(article: List<ArticleResponse>) =
    article.map { ArticleDto(it.title) }

class RepositoryImpl @Inject constructor(
    private val remoteApi: RemoteApi
) : Repository {

    override fun getList(): Flow<List<ArticleDto>> {
        return flow {
            remoteApi.getList().suspendOperator(
                ResponseBaseOperator(
                    mapper = ::mapperToDto,
                    onSuccess = ::emit)
            )
        }
    }
}


