package kr.co.architecture.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import kr.co.architecture.core.network.model.PicsumImagesApiResponse.PicsumImagesApiResponseItem
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.AUTHOR
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.DOWNLOAD_URL
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.HEIGHT
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.WIDTH
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.ID
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.URL
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Method.GET
import kr.co.architecture.custom.http.client.RawHttp11Client

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
        // TODO: 코드를 더 간소화할 순 없을까?
        rawHttp11Client.callApi(
          method = GET,
          url = "$url$path?page=$page&limit=$limit",
          onResponseSuccess = {
            val jsonArray = JSONArray(body.toString(Charset.forName("UTF-8")))
            val result = PicsumImagesApiResponse().apply {
              for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                add(
                  PicsumImagesApiResponseItem(
                    id = jsonObject.getString(ID),
                    author = jsonObject.getString(AUTHOR),
                    width = jsonObject.getInt(WIDTH),
                    height = jsonObject.getInt(HEIGHT),
                    url = jsonObject.getString(URL),
                    downloadUrl = jsonObject.getString(DOWNLOAD_URL)
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