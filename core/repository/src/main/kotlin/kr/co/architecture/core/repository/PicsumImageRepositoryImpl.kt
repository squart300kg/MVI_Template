package kr.co.architecture.core.repository

import kr.co.architecture.core.network.PicsumApi
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.repository.dto.PicsumImagesDto
import kr.co.architecture.core.repository.dto.PicsumImagesDto.Image
import javax.inject.Inject

class PicsumImageRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : PicsumImageRepository {

  override suspend fun getPicsumImages(page: Int): PicsumImagesDto {
    val api = PicsumApi()
    val response = api.getList(1, 30)
    return PicsumImagesDto(
      items = response.items.map {
        Image(
          author = it.author,
          downloadUrl = it.downloadUrl,
          width = it.width,
          height = it.height,
          id = it.id,
          url = it.url
        )
      },
      hasNext = response.next?.isNotEmpty() == true,
    )
//    return remoteApi.getPicsumImages(page = page)
//      .getOrThrowAppFailure()
//      .let(PicsumImagesDto::mapperToDto)
  }
}


