package kr.co.architecture.core.repository

import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.safeGet
import kr.co.architecture.core.network.operator.throwIfNull
import kr.co.architecture.core.repository.dto.AlimListRequestDto
import kr.co.architecture.core.repository.dto.AlimListResponseDto
import javax.inject.Inject

class AlimRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
) : AlimRepository {

  override suspend fun getAlimList(dto: AlimListRequestDto): AlimListResponseDto {
    return remoteApi.getAlimList()
      .safeGet()
      ?.let(AlimListResponseDto::mapperToDto)
      .throwIfNull()
  }

}


