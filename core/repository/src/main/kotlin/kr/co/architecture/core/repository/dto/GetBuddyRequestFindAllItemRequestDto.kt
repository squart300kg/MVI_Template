package kr.co.architecture.core.repository.dto

import com.google.gson.JsonObject
import kr.co.architecture.core.network.model.GetBuddyRequestFindAllItemRequestEntity

data class GetBuddyRequestFindAllItemRequestDto(
  val buddyRequestType: String,
  val phoneNums: JsonObject,
  val updateFlag: Boolean,
  val userId: Int,
  val page: Int
) {
  companion object {
    fun mapperToEntity(dto: GetBuddyRequestFindAllItemRequestDto) =
      GetBuddyRequestFindAllItemRequestEntity(
        buddyRequestType = dto.buddyRequestType,
        phoneNums = dto.phoneNums,
        updateFlag = dto.updateFlag,
        userId = dto.userId,
        page = dto.page
      )
  }
}
