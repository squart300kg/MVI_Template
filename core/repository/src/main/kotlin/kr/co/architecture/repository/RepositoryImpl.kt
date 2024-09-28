package kr.co.architecture.repository

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.architecture.network.RemoteApi
import kr.co.architecture.network.model.Response
import javax.inject.Inject

data class DataModel(
    val item: List<Item>
) {
    data class Item(
        val name: String
    )
}

fun convertDataItem(article: Response.Article) =
    DataModel.Item(
        name = article.title
    )

class RepositoryImpl @Inject constructor(
    private val remoteApi: RemoteApi
) : Repository {

    override val list: Flow<Result<DataModel>> = flow {
        when (val response = remoteApi.getList()) {
            is ApiResponse.Success -> {
                val dataModel = response.data.articles.map(::convertDataItem).let(::DataModel)
                emit(Result.success(dataModel))
            }
            is ApiResponse.Failure.Exception -> emit(Result.failure(response.exception))
            is ApiResponse.Failure.Error -> emit(Result.failure(Exception(response.message())))
        }

    }
}


