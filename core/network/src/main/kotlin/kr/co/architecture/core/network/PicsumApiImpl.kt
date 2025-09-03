package kr.co.architecture.core.network

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
): PicsumApi {

  override suspend fun getPicsumImages(
    path: String,
    page: Int,
    limit: Int
  ): ApiResponse<PicsumImagesApiResponse> {
    return try {
      val response = rawHttp11Client.callApi(
        method = GET,
        url = "$url$path?page=$page&limit=$limit"
      )
      if (response.code != 200) {
        return ApiResponse.Error(
          code = response.code,
          message = response.message,
          errorBody = response.body.toString(Charset.forName("UTF-8"))
        )
      }

      // JSON 파싱 (org.json 사용; 외부 JSON 라이브러리 사용 금지 제약 준수)
      val jsonArray = JSONArray(response.body.toString(Charset.forName("UTF-8")))
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
      ApiResponse.Success(
        code = response.code,
        message = response.message,
        header = response.headers,
        data = result
      )
    } catch (e: Exception) {
      ApiResponse.Exception(e)
    }
  }
}