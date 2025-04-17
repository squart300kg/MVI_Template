package kr.co.architecture.core.repository

import com.skydoves.sandwich.suspendOperator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.model.ArticleResponse
import kr.co.architecture.core.network.model.CommonResponse
import kr.co.architecture.core.network.operator.ResponseBaseOperator
import kr.co.architecture.core.repository.dto.ArticleDto
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : Repository {

  override fun getList(): Flow<List<ArticleDto>> {
    return flow {
      remoteApi.getList().suspendOperator(
        ResponseBaseOperator(
          mapper = ArticleDto::mapperToDto,
          onSuccess = ::emit
        )
      )
    }
  }
}


