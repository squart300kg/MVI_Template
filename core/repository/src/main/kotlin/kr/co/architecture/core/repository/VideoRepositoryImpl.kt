package kr.co.architecture.core.repository

import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.VideoDto
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : VideoRepository {

  override suspend fun getVideos(query: ContentsQuery): VideoDto {
    return remoteApi.getVideos(
      query = query.query,
      page = query.page
    )
      .getOrThrowAppFailure()
      .let(VideoDto::mapperToDto)
  }

  override suspend fun toggleBookmark(contents: MediaContents, toggleType: ToggleTypeEnum) {
    TODO("Not yet implemented")
  }
}


