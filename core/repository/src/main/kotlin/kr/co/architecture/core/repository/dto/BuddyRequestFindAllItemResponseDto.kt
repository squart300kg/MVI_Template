package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.BuddyRequestFindAllItemEntity
import java.util.Date

data class BuddyRequestFindAllItemResponseDto(
  val list: List<BuddyRequestFindAllDto>,
  val pageable: Boolean?,
  val totalCnt: Int?
) {

  companion object {
    fun mapperToDto(entity: BuddyRequestFindAllItemEntity) =
      BuddyRequestFindAllItemResponseDto(
        list = entity.list?.map {
          BuddyRequestFindAllDto(
            userId = it.userId,
            buddyCnt = it.buddyCnt,
            buddyRequestId = it.buddyRequestId,
            buddyType = it.buddyType,
            channel = it.channel,
            greetings = it.greetings,
            nickname = it.nickname,
            phoneNum = it.phoneNum,
            readYN = it.readYN,
            userMail = it.userMail,
            username = it.username,
            youtubeLink = it.youtubeLink,
            imogiCd = it.imogiCd,
            totalCnt = it.totalCnt,
            withBuddyCount = it.withBuddyCount,
            withBuddyImogiCdList = it.withBuddyImogiCdList,
            requestDate = it.requestDate,
          )
        } ?: emptyList(),
        pageable = entity.pageable,
        totalCnt = entity.totalCnt,
      )
  }

  data class BuddyRequestFindAllDto(
    val userId: Int,
    val buddyCnt: Int?,
    val buddyRequestId: Int,
    val buddyType: String?,
    val channel: String?,
    val greetings: String?,
    val nickname: String?,
    val phoneNum: String?,
    val readYN: String?,
    val userMail: String?,
    val username: String?,
    val youtubeLink: String?,
    val imogiCd: String?,
    val totalCnt: Int?,
    val withBuddyCount: Int,
    val withBuddyImogiCdList: List<String>?,
    val requestDate: Date
  )
}