package kr.co.architecture.core.repository

import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.VideoDto
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : VideoRepository {

  override suspend fun getVideos(): VideoDto {
    return remoteApi.getVideos(
      query = "빠더너스",
      page = 1
    )
      .getOrThrowAppFailure()
      .let(VideoDto::mapperToDto)
  }
}


