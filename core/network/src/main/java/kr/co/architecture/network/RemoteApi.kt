package kr.co.architecture.network

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.network.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApi {

    @GET("v2/top-headlines")
    suspend fun getList(
        @Query("apikey") apiKey: String = BuildConfig.apiKey,
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 10,
    ): ApiResponse<Response>
}