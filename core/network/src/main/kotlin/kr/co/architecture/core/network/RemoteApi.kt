package kr.co.architecture.core.network

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.core.network.BuildConfig
import kr.co.architecture.core.network.model.ArticleResponse
import kr.co.architecture.core.network.model.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApi {

    @GET("v2/top-headlines")
    suspend fun getList(
        @Query("apikey") apiKey: String = BuildConfig.apiKey,
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 10,
    ): ApiResponse<CommonResponse<ArticleResponse>>
}