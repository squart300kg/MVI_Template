package kr.co.architecture.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Method.GET
import kr.co.architecture.core.network.httpClient.RawHttp11Client
import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

class PicsumApiImpl(
  val rawHttp11Client: RawHttp11Client = RawHttp11Client(),
  val url: String
) : PicsumApi {

  override suspend fun getPicsumImages(
    path: String,
    page: Int,
    limit: Int
  ): ApiResponse<PicsumImagesApiResponse> {
    return try {
      callbackFlow {
        rawHttp11Client.callApi(
          method = GET,
          url = "$url$path?page=$page&limit=$limit",
          onResponseSuccess = {
            val jsonArray = JSONArray(body.toString(Charset.forName("UTF-8")))
            val result = PicsumImagesApiResponse().apply {
              for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                add(
                  PicsumImagesApiResponse.PicsumImagesApiResponseItem(
                    id = jsonObject.getString("id"),
                    author = jsonObject.getString("author"),
                    width = jsonObject.getInt("width"),
                    height = jsonObject.getInt("height"),
                    url = jsonObject.getString("url"),
                    downloadUrl = jsonObject.getString("download_url")
                  )
                )
              }
            }
            send(
              element = ApiResponse.Success(
                code = code,
                message = message,
                header = headers,
                data = result
              )
            )
          },
          onResponseError = {
            send(
              element = ApiResponse.Error(
                code = code,
                message = message,
                errorBody = body.toString(Charset.forName("UTF-8"))
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