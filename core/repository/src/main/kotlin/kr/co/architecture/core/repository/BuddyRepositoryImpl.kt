package kr.co.architecture.core.repository

import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.safeGet
import kr.co.architecture.core.network.operator.throwIfNull
import kr.co.architecture.core.repository.dto.AlimListRequestDto
import kr.co.architecture.core.repository.dto.AlimListResponseDto
import kr.co.architecture.core.repository.dto.BuddyRequestFindAllItemResponseDto
import kr.co.architecture.core.repository.dto.GetBuddyRequestFindAllItemRequestDto
import javax.inject.Inject

class BuddyRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi
): BuddyRepository {
  override suspend fun getBuddyRequestFindAllItem(body: GetBuddyRequestFindAllItemRequestDto): BuddyRequestFindAllItemResponseDto {
    return remoteApi.getBuddyRequestFindAllItem()
      .safeGet()
      ?.let(BuddyRequestFindAllItemResponseDto::mapperToDto)
      .throwIfNull()
  }
}