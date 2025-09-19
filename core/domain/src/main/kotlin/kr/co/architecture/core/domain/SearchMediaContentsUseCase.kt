package kr.co.architecture.core.domain

import kr.co.architecture.core.domain.formatter.KoreanDateTextFormatter
import kr.co.architecture.core.domain.mapper.MediaContentsMapper
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.repository.ImageRepository
import kr.co.architecture.core.repository.VideoRepository
import kr.co.architecture.core.repository.dto.ImageDto.Image
import kr.co.architecture.core.repository.dto.VideoDto.Video
import javax.inject.Inject

class SearchMediaContentsUseCase @Inject constructor(
  private val imageRepository: ImageRepository,
  private val videoRepository: VideoRepository,
  private val dateTextFormatter: KoreanDateTextFormatter
) {
  suspend operator fun invoke(id: String): MediaContents? {
    return imageRepository.searchImages(id)
      ?.let { image ->
        MediaContentsMapper.mapperToDomain(
          image = image,
          dateTextFormatter = dateTextFormatter
        )
      } ?: run {
      videoRepository.searchVideo(id)
        ?.let { video ->
          MediaContentsMapper.mapperToDomain(
            video = video,
            dateTextFormatter = dateTextFormatter
          )
        }
    }
  }
}