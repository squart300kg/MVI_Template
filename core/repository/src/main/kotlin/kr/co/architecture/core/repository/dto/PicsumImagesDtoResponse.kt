package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiHeaderField.LINK
import kr.co.architecture.core.network.model.PicsumImagesApiResponse
import java.net.URL

data class PicsumImagesDtoResponse(
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
    fun mapperToDto(
      apiResponse: ApiResponse.Success<PicsumImagesApiResponse>,
      dtoRequest: PicsumImagesDtoRequest
    ) =
      PicsumImagesDtoResponse(
        items = apiResponse.data.map {
          Image(
            author = it.author,
            downloadUrl = with(URL(it.downloadUrl)) {
              val segments = path.split('/')
              val idKey = segments.getOrNull(1)
              val idValue = segments.getOrNull(2)
              if (idKey != null && idValue != null) "$protocol://$host/${idKey}/${idValue}/${dtoRequest.requestSize}/${dtoRequest.requestSize}"
              else it.downloadUrl

              it.downloadUrl
            },
            width = it.width,
            height = it.height,
            id = it.id,
            url = it.url
          )
        },
        hasNext = apiResponse.header[LINK]
          ?.contains("rel=\"next\"") == true
      )
  }
}