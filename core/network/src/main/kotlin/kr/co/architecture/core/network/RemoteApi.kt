package kr.co.architecture.core.network

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApi {

  @GET("v2/list")
  suspend fun getPicsumImages(
    @Query("page") page: Int = 1,
    @Query("limit") limit: Int = 30,
  ): ApiResponse<PicsumImagesApiResponse>
}