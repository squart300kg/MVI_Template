package kr.co.architecture.core.domain

import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.repository.ImageRepository
import kr.co.architecture.core.repository.VideoRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
  private val imageRepository: ImageRepository,
  private val videoRepository: VideoRepository
) {
  suspend operator fun invoke(params: Params) {
    when (params.mediaContents.mediaContentsType) {
      MediaContentsTypeEnum.IMAGE -> imageRepository.toggleBookmark(
        contents = params.mediaContents,
        toggleType = params.toggleType
      )
      MediaContentsTypeEnum.VIDEO -> videoRepository.toggleBookmark(
        contents = params.mediaContents,
        toggleType = params.toggleType
      )
    }
  }

  data class Params(
    val toggleType: ToggleTypeEnum,
    val mediaContents: MediaContents
  )
}