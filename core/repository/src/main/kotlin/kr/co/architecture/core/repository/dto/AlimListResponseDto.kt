package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.network.model.AlimCenterEntity
import java.util.Date

data class AlimListResponseDto(
  val alimNoticeDto: List<AlimDto>,
  val alimSalesHistoryDto: List<AlimDto>,
  val alimActivityDto: List<AlimDto>,
  val moreNotice: Boolean,
  val moreTrading: Boolean,
  val moreActivity: Boolean
) {
  companion object {
    fun mapperToDto(entity: AlimCenterEntity): AlimListResponseDto {
      return AlimListResponseDto(
        alimNoticeDto = entity.noticeDto.map {
          AlimDto.Notice(
            noticeId = it.noticeId,
            contentsType = it.contentsType,
            contents = it.contents,
            nickname = it.nickname,
            imogiCd = it.imogiCd,
            subcommentContent = it.commentContents,
            readYn = it.readYn,
            moreViewUrl = "https://www.naver.com",
            createdDate = it.createdDate
          )
        },
        alimSalesHistoryDto = entity.tradingDto.map {
          AlimDto.Trading(
            contentsType = it.contentsType,
            contents = it.contents,
            nickname = it.nickname,
            imogiCd = it.imogiCd,
            tradingId = it.tradingId,
            readYn = it.readYn,
            createdDate = it.createdDate,
          )
        },
        alimActivityDto = entity.activityDto.map {
          AlimDto.Activity(
            userId = it.userId,
            contentsType = it.contentsType,
            contents = it.contents,
            nickname = it.nickname,
            imogiCd = it.imogiCd,
            buddyUserId = it.buddyUserId,
            buddyType = it.buddyType,
            buddyCnt = it.buddyCnt,
            commentId = it.commentId,
            subCommentId = it.subCommentId,
            tradingId = it.tradingId,
            readYn = it.readYn,
            subcommentContent = it.commentContents,
            createdDate = it.createdDate,
          )
        },
        moreNotice = entity.moreNotice,
        moreTrading = entity.moreTrading,
        moreActivity = entity.moreActivity,
      )
    }
  }

  sealed interface AlimDto {
    data class Notice(
      val noticeId: Int?,
      val contentsType: String,
      val contents: String,
      val subcommentContent: String?,
      val nickname: String,
      val imogiCd: String,
      val readYn: String,
      val moreViewUrl: String,
      val createdDate: Date
    ): AlimDto
    data class Trading(
      val contentsType: String,
      val contents: String,
      val nickname: String,
      val imogiCd: String,
      val tradingId: Int,
      val readYn: String,
      val createdDate: Date,
    ): AlimDto
    data class Activity(
      val userId: Int,
      val contentsType: String,
      val contents: String,
      val subcommentContent: String?,
      val nickname: String,
      val imogiCd: String,
      val buddyUserId: Int,
      val buddyType: String?,
      val buddyCnt: Int?,
      val commentId: Int?,
      val subCommentId: Int?,
      val tradingId: Int,
      val readYn: String,
      val createdDate: Date,
    ): AlimDto
  }
}