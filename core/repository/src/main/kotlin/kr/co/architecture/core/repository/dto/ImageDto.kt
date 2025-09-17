package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.CommonApiResponse

data class ImageDto(
  val images: List<Image>,
  val pageable: PageableDto
) {
  data class Image(
    val collection: String,
    val dateTime: String,
    val displaySiteName: String,
    val docUrl: String,
    val height: Int,
    val imageUrl: String,
    val thumbnailUrl: String,
    val width: Int
  )
  companion object {
    fun mapperToDto(apiResponse: CommonApiResponse<kr.co.architecture.core.network.model.Image>) = ImageDto(
      images = apiResponse.documents.map(::mapperToDto),
      pageable = PageableDto(
        isEnd = apiResponse.meta.isEnd
      )
    )

    fun mapperToDto(apiResponse: kr.co.architecture.core.network.model.Image) = Image(
      collection = apiResponse.collection,
      dateTime = apiResponse.datetime,
      displaySiteName = apiResponse.displaySiteName,
      docUrl = apiResponse.docUrl,
      height = apiResponse.height,
      imageUrl = apiResponse.imageUrl,
      thumbnailUrl = apiResponse.thumbnailUrl,
      width = apiResponse.width
    )
  }
}
