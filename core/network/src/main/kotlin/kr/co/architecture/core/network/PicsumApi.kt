package kr.co.architecture.core.network

import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse

interface PicsumApi {

  suspend fun getPicsumImages(path: String, page: Int, limit: Int): ApiResponse<PicsumImagesApiResponse>

}