package kr.co.architecture.core.repository

import kr.co.architecture.core.network.PicsumApi
import kr.co.architecture.core.network.PicsumApiImpl
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.repository.dto.PicsumImagesDto
import javax.inject.Inject

class PicsumImageRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : PicsumImageRepository {

  override suspend fun getPicsumImages(page: Int): PicsumImagesDto {
    val remoteApi: PicsumApi = PicsumApiImpl(
      url = "https://picsum.photos/"
    )
    return remoteApi.getPicsumImages(
      path = "v2/list",
      page = 1,
      limit = 30
    ).let(PicsumImagesDto::mapperToDto)
  }
}


