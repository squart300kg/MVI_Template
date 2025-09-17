package kr.co.architecture.core.repository

import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.ImageDto
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : ImageRepository {

  override suspend fun getImages(query: ContentsQuery): ImageDto {
    return remoteApi.getImages(
      query = query.query,
      page = query.page
    )
      .getOrThrowAppFailure()
      .let(ImageDto::mapperToDto)
  }
}
