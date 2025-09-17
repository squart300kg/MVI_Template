package kr.co.architecture.core.repository

import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.ImageDto
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : ImageRepository {

  override suspend fun getImages(): ImageDto {
    return remoteApi.getImages(
      query = "빠더너스",
      page = 1
    )
      .getOrThrowAppFailure()
      .let(ImageDto::mapperToDto)
  }
}


