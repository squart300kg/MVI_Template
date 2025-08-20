package kr.co.architecture.core.network.model

import java.util.Date

data class AlimCenterEntity(
  val noticeDto: List<AlimNoticeEntity>,
  val tradingDto: List<AlimTradingEntity>,
  val activityDto: List<AlimActivityEntity>,
  val moreNotice: Boolean,
  val moreTrading: Boolean,
  val moreActivity: Boolean
) {
  data class AlimNoticeEntity(
    val notiId: Int,
    val userId: Int,
    val contentsType: String,
    val contents: String,
    val nickname: String,
    val imogiCd: String,
    val buddyUserId: Int,
    val buddyType: String?,
    val buddyCnt: Int?,
    val commentId: Int?,
    val subCommentId: Int?,
    val tradingId: Int,
    val noticeId: Int?,
    val advertiseId: Int?,
    val readYn: String,
    val commentContents: String?,
    val createdDate: Date,
    val removeBuddy: String,
    val tradingOwnerId: Int
  )

  data class AlimTradingEntity(
    val notiId: Int,
    val userId: Int,
    val contentsType: String,
    val contents: String,
    val nickname: String,
    val imogiCd: String,
    val buddyUserId: Int,
    val buddyType: String?,
    val buddyCnt: Int?,
    val commentId: Int?,
    val subCommentId: Int?,
    val tradingId: Int,
    val noticeId: Int?,
    val advertiseId: Int?,
    val readYn: String,
    val commentContents: String?,
    val createdDate: Date,
    val removeBuddy: String,
    val tradingOwnerId: Int
  )

  data class AlimActivityEntity(
    val notiId: Int,
    val userId: Int,
    val contentsType: String,
    val contents: String,
    val nickname: String,
    val imogiCd: String,
    val buddyUserId: Int,
    val buddyType: String?,
    val buddyCnt: Int?,
    val commentId: Int?,
    val subCommentId: Int?,
    val tradingId: Int,
    val noticeId: Int?,
    val advertiseId: Int?,
    val readYn: String,
    val commentContents: String?,
    val createdDate: Date,
    val removeBuddy: String,
    val tradingOwnerId: Int
  )
}