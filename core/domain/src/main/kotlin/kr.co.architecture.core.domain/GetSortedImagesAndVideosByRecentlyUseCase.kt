package kr.co.architecture.core.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.repository.ImageRepository
import kr.co.architecture.core.repository.VideoRepository
import kr.co.architecture.core.repository.dto.ImageDto
import kr.co.architecture.core.repository.dto.PageableDto
import kr.co.architecture.core.repository.dto.VideoDto
import javax.inject.Inject

class GetSortedImagesAndVideosByRecentlyUseCase @Inject constructor(
  private val imageRepository: ImageRepository,
  private val videoRepository: VideoRepository
) {
  suspend operator fun invoke(query: ContentsQuery): Response {
    return coroutineScope {
      // TODO: 비동기 처리 전략 고민
      val imageDto = async {
        imageRepository.getImages(query)
      }.await()
        .let(Response::mapperToResponse)

      val videoDto = async {
        videoRepository.getVideos(query)
      }.await()
        .let(Response::mapperToResponse)

      Response(
        contentsList = (imageDto.contentsList + videoDto.contentsList).sortedBy { it.dateTime },
        pageableDto = imageDto.pageableDto
      )
    }
  }

  data class Response(
    val contentsList: List<Contents>,
    val pageableDto: PageableDto
  ) {
    data class Contents(
      val thumbnailUrl: String,
      val dateTime: String,
      val title: String,
      val collection: String? = null,
      val contents: String
    )

    companion object {
      fun mapperToResponse(imageDto: ImageDto) = Response(
        contentsList = imageDto.images.map {
          Contents(
            thumbnailUrl = it.thumbnailUrl,
            dateTime = it.dateTime,
            title = it.displaySiteName,
            contents = it.docUrl,
            collection = it.collection
          )
        },
        pageableDto = imageDto.pageable
      )
      fun mapperToResponse(videoDto: VideoDto) = Response(
        contentsList = videoDto.videos.map {
          Contents(
            thumbnailUrl = it.thumbnail,
            dateTime = it.datetime,
            title = it.title,
            contents = it.url
          )
        },
        pageableDto = videoDto.pageable
      )
    }
  }
}