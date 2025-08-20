package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.BuddyRequestFindAllItemResponseDto
import kr.co.architecture.core.repository.dto.GetBuddyRequestFindAllItemRequestDto

interface BuddyRepository {

  suspend fun getBuddyRequestFindAllItem(body: GetBuddyRequestFindAllItemRequestDto): BuddyRequestFindAllItemResponseDto

}