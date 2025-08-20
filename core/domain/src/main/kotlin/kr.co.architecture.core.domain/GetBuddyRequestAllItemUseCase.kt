package kr.co.architecture.core.domain

import com.google.gson.JsonObject
import kr.co.architecture.core.model.enums.BuddyRequestTypeEnum
import kr.co.architecture.core.repository.BuddyRepository
import kr.co.architecture.core.repository.dto.BuddyRequestFindAllItemResponseDto
import kr.co.architecture.core.repository.dto.GetBuddyRequestFindAllItemRequestDto
import javax.inject.Inject

class GetBuddyRequestAllItemUseCase @Inject constructor(
  private val buddyRepository: BuddyRepository,
  private val getMyContactListAsJsonObjectUseCase: GetMyContactListAsJsonObjectUseCase,
) {
  suspend operator fun invoke(buddyRequestTypeEnum: BuddyRequestTypeEnum): BuddyRequestFindAllItemResponseDto {
    return invoke(
      jsonPhoneNumber = getMyContactListAsJsonObjectUseCase(),
      buddyRequestTypeEnum = buddyRequestTypeEnum
    )
  }

  suspend operator fun invoke(
    jsonPhoneNumber: JsonObject,
    buddyRequestTypeEnum: BuddyRequestTypeEnum
  ): BuddyRequestFindAllItemResponseDto {
    return buddyRepository.getBuddyRequestFindAllItem(
      body = GetBuddyRequestFindAllItemRequestDto(
        buddyRequestType = buddyRequestTypeEnum.code,
        phoneNums = jsonPhoneNumber,
        updateFlag = false,
        userId = 9999,
        page = 0,
      )
    )
  }
}