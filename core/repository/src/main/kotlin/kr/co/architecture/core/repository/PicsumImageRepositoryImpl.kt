package kr.co.architecture.core.repository

import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.PicsumImageDto
import javax.inject.Inject

class PicsumImageRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : PicsumImageRepository {

  override suspend fun getPicsumImages(page: Int): List<PicsumImageDto> {
    return remoteApi.getPicsumImages(page = page)
      .getOrThrowAppFailure()
      .let(PicsumImageDto::mapperToDto)
  }
}


