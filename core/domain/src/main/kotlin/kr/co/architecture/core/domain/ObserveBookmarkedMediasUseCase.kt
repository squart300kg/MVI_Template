package kr.co.architecture.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kr.co.architecture.core.domain.formatter.DateTextFormatter
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.repository.ImageRepository
import kr.co.architecture.core.repository.VideoRepository
import javax.inject.Inject

class ObserveBookmarkedMediasUseCase @Inject constructor(
  private val imageRepository: ImageRepository,
  private val videoRepository: VideoRepository
) {
  operator fun invoke(): Flow<Set<MediaContents>> =
    combine(
      flow = imageRepository.observeBookmarkedMedias(),
      flow2 = videoRepository.observeBookmarkedMedias()
    ) { images, videos ->
      images + videos
    }.distinctUntilChanged()
}