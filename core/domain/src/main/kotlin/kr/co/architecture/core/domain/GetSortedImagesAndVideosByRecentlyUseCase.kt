package kr.co.architecture.core.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kr.co.architecture.core.domain.formatter.DateTextFormatter
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.repository.ImageRepository
import kr.co.architecture.core.repository.VideoRepository
import kr.co.architecture.core.repository.dto.ImageDto
import kr.co.architecture.core.repository.dto.PageableDto
import kr.co.architecture.core.repository.dto.VideoDto
import javax.inject.Inject

class GetSortedImagesAndVideosByRecentlyUseCase @Inject constructor(
  private val imageRepository: ImageRepository,
  private val videoRepository: VideoRepository,
  private val dateTextFormatter: DateTextFormatter
) {
  // TODO: 이거 제일 중요함
  /**
   * 1. 둘 다 에러가 날 경우, 에러를 throw한다.
   * 2. 둘중 한 쪽만 에러날 경우, 에러가 나지 않은 쪽의 결과만 준다. (에러난 쪽은 emptyList로)
   * 3. 둘중 한 쪽이 데이터가 먼저 없을 경우(페이징 끝까지 다다름), 남아있는 데이터만 준다.
   * 4. 위 3번 후, 데이터가 남아있는 쪽의 API만 호출한다. 페이징 끝까지 다다른 API는 호출하지 않는다.
   */
  suspend operator fun invoke(query: ContentsQuery): Response {
    return coroutineScope {
      // TODO: 비동기 처리 전략 고민
      val imageDto = async {
        imageRepository.getImages(query)
      }.await()
        .let {
          Response.mapperToResponse(
            imageDto = it,
            dateTextFormatter = dateTextFormatter
          )
        }

      val videoDto = async {
        videoRepository.getVideos(query)
      }.await()
        .let {
          Response.mapperToResponse(
            videoDto = it,
            dateTextFormatter = dateTextFormatter
          )
        }

      Response(
        mediaContentsList = (imageDto.mediaContentsList + videoDto.mediaContentsList).sortedBy { it.dateTime },
        pageableDto = imageDto.pageableDto
      )
    }
  }

  data class Response(
    val mediaContentsList: List<MediaContents>,
    val pageableDto: PageableDto
  ) {
    companion object {
      fun mapperToResponse(
        imageDto: ImageDto,
        dateTextFormatter: DateTextFormatter
      ) = Response(
        mediaContentsList = imageDto.images.map {
          MediaContents(
            thumbnailUrl = it.thumbnailUrl,
            dateTime = dateTextFormatter(it.dateTime),
            title = it.displaySiteName,
            contents = it.docUrl,
            collection = it.collection,
            mediaContentsType = MediaContentsTypeEnum.IMAGE
          )
        },
        pageableDto = imageDto.pageable
      )
      fun mapperToResponse(
        videoDto: VideoDto,
        dateTextFormatter: DateTextFormatter
      ) = Response(
        mediaContentsList = videoDto.videos.map {
          MediaContents(
            thumbnailUrl = it.thumbnail,
            dateTime = dateTextFormatter(it.datetime),
            title = it.title,
            contents = it.url,
            mediaContentsType = MediaContentsTypeEnum.VIDEO
          )
        },
        pageableDto = videoDto.pageable
      )
    }
  }
}