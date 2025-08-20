package kr.co.architecture.core.network.model

import java.util.Date

data class BuddyRequestFindAllItemEntity(
  val list: List<BuddyRequestFindAllEntity>?,
  val pageable: Boolean?,
  val totalCnt: Int?
) {
  data class BuddyRequestFindAllEntity(
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
