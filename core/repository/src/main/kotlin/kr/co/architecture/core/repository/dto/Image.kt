package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse

data class PicsumImagesDto(
  val items: List<Image>,
  val hasNext: Boolean
) {
  data class Image(
    val author: String,
    val downloadUrl: String,
    val width: Int,
    val height: Int,
    val id: String,
    val url: String
  )
  companion object {
    fun mapperToDto(apiResponse: ApiResponse.Success<PicsumImagesApiResponse>) =
      PicsumImagesDto(
        items = apiResponse.data.map {
          Image(
            author = it.author,
            downloadUrl = it.downloadUrl,
            width = it.width,
            height = it.height,
            id = it.id,
            url = it.url
          )
        },
        // TODO: 상수로 잘 보관하는법 찾기
        hasNext = apiResponse.header["link"]
          ?.contains("rel=\"next\"") == true
      )
  }
}