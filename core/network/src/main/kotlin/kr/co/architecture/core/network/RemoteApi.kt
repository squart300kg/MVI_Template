package kr.co.architecture.core.network

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.core.network.model.SearchedBookApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApi {

  @GET("v3/search/book")
  suspend fun searchBook(
    @Query("query") query: String,
    @Query("sort") sort: String,
    @Query("page") page: Int,
    @Query("size") size: Int = 20,
    @Query("target") target: Int? = null,
  ): ApiResponse<SearchedBookApiResponse>
}