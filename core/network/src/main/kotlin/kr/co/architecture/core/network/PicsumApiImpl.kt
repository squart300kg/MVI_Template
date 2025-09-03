package kr.co.architecture.core.network

import kr.co.architecture.core.network.httpClient.HttpHeaderConstants.Value.APPLICATION_JSON
import kr.co.architecture.core.network.httpClient.RawHttp11Client
import kr.co.architecture.core.network.httpClient.parseLinkHeader
import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.Locale

class PicsumApiImpl(
  val rawHttp11Client: RawHttp11Client = RawHttp11Client(),
  val url: String
): PicsumApi {

  override suspend fun getPicsumImages(
    path: String,
    page: Int,
    limit: Int
  ): ApiResponse<PicsumImagesApiResponse> {
    val url = "$url$path?page=$page&limit=$limit"
    val response = rawHttp11Client.get(
      url = url,
      headers = emptyMap() // 필요 시 커스텀 헤더 추가
    )
    if (response.code != 200) throw IOException("HTTP ${response.code} ${response.message}")

    // 헤더: content-type 검증(선택)
    val contentType = response.headers["content-type"]?.lowercase(Locale.US)
    if (contentType != null && !contentType.startsWith(APPLICATION_JSON)) {
      throw IOException("Unexpected content-type: $contentType")
    }

    // 캐시 제어: no-store → 목록은 캐시하지 않음 (네가 준 헤더를 존중)
    // val cacheControl = resp.headers["cache-control"]

    // Link 파싱 (prev/next)
    val linkMap = parseLinkHeader(response.headers["link"])
    val prev = linkMap["prev"]
    val next = linkMap["next"]

    // JSON 파싱 (org.json 사용; 외부 JSON 라이브러리 사용 금지 제약 준수)
    val jsonArray = JSONArray(response.body.toString(Charset.forName("UTF-8")))
    val items = buildList(jsonArray.length()) {
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
    return ApiResponse(
      code = response.code,
      message = response.message,
      header = response.headers,
      body = result
    )
  }
}