package kr.co.architecture.core.network

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.core.network.constants.ApiConstants.Path.V2_SEARCH_IMAGE
import kr.co.architecture.core.network.constants.ApiConstants.Path.V2_SEARCH_VCLIP
import kr.co.architecture.core.network.constants.ApiConstants.Query.Key.PAGE
import kr.co.architecture.core.network.constants.ApiConstants.Query.Key.QUERY
import kr.co.architecture.core.network.constants.ApiConstants.Query.Key.SIZE
import kr.co.architecture.core.network.constants.ApiConstants.Query.Key.SORT
import kr.co.architecture.core.network.constants.ApiConstants.Query.Value.DEFAULT_SIZE
import kr.co.architecture.core.network.constants.ApiConstants.Query.Value.DEFAULT_SORT
import kr.co.architecture.core.network.model.CommonApiResponse
import kr.co.architecture.core.network.model.Image
import kr.co.architecture.core.network.model.Video
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApi {
  @GET(V2_SEARCH_IMAGE)
  suspend fun getImages(
    @Query(QUERY) query: String,
    @Query(PAGE) page: Int,
    @Query(SORT) sort: String = DEFAULT_SORT,
    @Query(SIZE) size: Int = DEFAULT_SIZE,
  ): ApiResponse<CommonApiResponse<Image>>

  @GET(V2_SEARCH_VCLIP)
  suspend fun getVideos(
    @Query(QUERY) query: String,
    @Query(PAGE) page: Int,
    @Query(SORT) sort: String = DEFAULT_SORT,
    @Query(SIZE) size: Int = DEFAULT_SIZE,
  ): ApiResponse<CommonApiResponse<Video>>
}