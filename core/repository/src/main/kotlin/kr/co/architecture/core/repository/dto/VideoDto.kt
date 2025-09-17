package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.CommonApiResponse

data class VideoDto(
  val videos: List<Video>,
  val pageable: PageableDto
) {
  data class Video(
    val author: String,
    val datetime: String,
    val playTime: Int,
    val thumbnail: String,
    val title: String,
    val url: String
  )
  companion object {
    fun mapperToDto(apiResponse: CommonApiResponse<kr.co.architecture.core.network.model.Video>) = VideoDto(
      videos = apiResponse.documents.map(::mapperToDto),
      pageable = PageableDto(
        isEnd = apiResponse.meta.isEnd
      )
    )

    private fun mapperToDto(apiResponse: kr.co.architecture.core.network.model.Video) = Video(
      author = apiResponse.author,
      datetime = apiResponse.datetime,
      playTime = apiResponse.playTime,
      thumbnail = apiResponse.thumbnail,
      title = apiResponse.title,
      url = apiResponse.url
    )
  }
}
