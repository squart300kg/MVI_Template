package kr.co.architecture.core.domain.mapper

import kr.co.architecture.core.domain.formatter.KoreanDateTextFormatter
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.repository.dto.ImageDto.Image
import kr.co.architecture.core.repository.dto.VideoDto.Video

object MediaContentsMapper {
  fun mapperToDomain(
    image: Image,
    dateTextFormatter: KoreanDateTextFormatter
  ) = MediaContents(
      thumbnailUrl = image.thumbnailUrl,
      dateTime = dateTextFormatter(image.dateTime),
      title = image.displaySiteName,
      collection = image.collection,
      contents = image.docUrl,
      mediaContentsType = MediaContentsTypeEnum.IMAGE
    )

  fun mapperToDomain(
    video: Video,
    dateTextFormatter: KoreanDateTextFormatter
  ) = MediaContents(
      thumbnailUrl = video.thumbnail,
      dateTime = dateTextFormatter(video.datetime),
      title = video.title,
      contents = video.url,
      mediaContentsType = MediaContentsTypeEnum.VIDEO
    )
}