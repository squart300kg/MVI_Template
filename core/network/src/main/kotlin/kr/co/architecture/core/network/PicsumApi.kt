package kr.co.architecture.core.network

import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumErrorApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse

interface PicsumApi {

  suspend fun getPicsumImages(
    path: String,
    page: Int,
    limit: Int = 30
  ): ApiResponse<PicsumImagesApiResponse, PicsumErrorApiResponse>

}