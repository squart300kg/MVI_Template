package kr.co.architecture.core.repository

import kr.co.architecture.core.network.PicsumApi
import kr.co.architecture.core.network.constants.ApiConstants.Path.V2_LIST
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.PicsumImagesDtoRequest
import kr.co.architecture.core.repository.dto.PicsumImagesDtoResponse
import javax.inject.Inject

class PicsumImageRepositoryImpl @Inject constructor(
  private val picsumApi: PicsumApi
) : PicsumImageRepository {

  override suspend fun getPicsumImages(dtoRequest: PicsumImagesDtoRequest): PicsumImagesDtoResponse {
    return picsumApi.getPicsumImages(
      path = V2_LIST,
      page = dtoRequest.page
    )
      .getOrThrowAppFailure()
      .let { apiResponse ->
        PicsumImagesDtoResponse.mapperToDto(
          apiResponse = apiResponse,
          dtoRequest = dtoRequest
        )
      }
  }
}