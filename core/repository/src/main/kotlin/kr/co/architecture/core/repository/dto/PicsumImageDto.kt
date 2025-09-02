package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.PicsumImagesApiResponse

data class PicsumImageDto(
  val author: String,
  val downloadUrl: String,
  val width: Int,
  val height: Int,
  val id: String,
  val url: String
) {
  companion object {
    fun mapperToDto(iamge: List<PicsumImagesApiResponse.PicsumImagesApiResponseItem>) =
      iamge.map {
        PicsumImageDto(
          author = it.author,
          downloadUrl = it.downloadUrl,
          width = it.width,
          height = it.height,
          id = it.id,
          url = it.url
        )
      }
  }
}

