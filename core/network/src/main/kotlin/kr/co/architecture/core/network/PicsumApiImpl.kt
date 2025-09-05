package kr.co.architecture.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumErrorApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Method.GET
import kr.co.architecture.custom.http.client.RawHttp11Client

class PicsumApiImpl(
  val rawHttp11Client: RawHttp11Client,
  val url: String
) : PicsumApi {

  override suspend fun getPicsumImages(
    path: String,
    page: Int,
    limit: Int
  ): ApiResponse<PicsumImagesApiResponse, PicsumErrorApiResponse> {
    return try {
      callbackFlow {
        rawHttp11Client.callApi(
          method = GET,
          url = "$url$path?page=$page&limit=$limit",
          onResponseSuccess = {
            send(
              element = ApiResponse.Success(
                code = code,
                message = message,
                header = header,
                data = PicsumImagesApiResponse.mapperToApiResponse(body.data)
              )
            )
          },
          onResponseError = {
            send(
              element = ApiResponse.Error(
                header = header,
                data = PicsumErrorApiResponse.mapperToApiResponse(body.data)
              )
            )
          },
          onResponseException = { throw this }
        )
        awaitClose()
      }.first()
    } catch (e: Exception) {
      ApiResponse.Exception(e)
    }
  }
}