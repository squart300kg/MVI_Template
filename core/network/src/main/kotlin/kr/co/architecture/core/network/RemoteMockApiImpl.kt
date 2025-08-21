package kr.co.architecture.core.network

import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.delay
import kr.co.architecture.core.network.model.AlimCenterEntity
import kr.co.architecture.core.network.model.AlimCenterEntity.AlimNoticeEntity
import kr.co.architecture.core.network.model.AlimCenterEntity.AlimTradingEntity
import kr.co.architecture.core.network.model.BuddyRequestFindAllItemEntity
import kr.co.architecture.core.network.model.BuddyRequestFindAllItemEntity.BuddyRequestFindAllEntity
import kr.co.architecture.core.network.operator.CommonApiResponse
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

class RemoteMockApiImpl @Inject constructor() : RemoteApi {
  override suspend fun getAlimList(): ApiResponse<CommonApiResponse<AlimCenterEntity>> {
    delay(1000L * Random.nextInt(from = 1, until = 4))
    return ApiResponse.suspendOf {
      CommonApiResponse(
        code = 200,
        message = "success",
        data = AlimCenterEntity(
          noticeDto = List(3) {
            AlimNoticeEntity(
              notiId = it,
              userId = it,
              contentsType = "NOTICE",
              contents = when {
                it % 3 == 0 -> "공지사항"
                it % 3 == 1 -> "개인정보 처리방침 변경"
                it % 3 == 2 -> "공지사항"
                else -> "개인정보 처리방침 변경"
              },
              nickname = "버디스탁",
              imogiCd = "EC010${it + 1}",
              buddyUserId = it,
              buddyType = "BR0101",
              buddyCnt = it,
              commentId = it,
              subCommentId = it,
              tradingId = it,
              noticeId = it,
              advertiseId = it,
              readYn = "Y",
              commentContents = when {
                it % 3 == 0 -> "공지사항 상세정보"
                it % 3 == 1 -> "개인정보 처리 방침이 변경되었어요."
                it % 3 == 2 -> "버디스탁 업데이트 안내"
                else -> "개인정보 처리 방침이 변경되었어요."
              },
              createdDate = Date(),
              removeBuddy = "",
              tradingOwnerId = it,
            )
          },
          tradingDto = List(5) {
            AlimTradingEntity(
              notiId = it,
              userId = it,
              contentsType =
                when {
                  it % 5 == 0 -> "TRADING"
                  it % 5 == 1 -> "TRADING"
                  it % 5 == 2 -> "TRADING"
                  it % 5 == 3 -> "TRADING"
                  it % 5 == 4 -> "TRADING"
                  else -> ""
                },
              contents = when {
                it % 5 == 0 -> "삼성전자를 51,000원에 매수했어요."
                it % 5 == 1 -> "삼성전자를 71,000원에 매수했어요."
                it % 5 == 2 -> "삼성전자를 13,400원에 매수했어요."
                it % 5 == 3 -> "삼성전자를 551,000원에 매수했어요."
                it % 5 == 4 -> "삼성전자를 151,000원에 매도했어요."
                else -> ""
              },
              nickname = when {
                it % 5 == 0 -> "둘리아빠"
                it % 5 == 1 -> "서에번쩍"
                it % 5 == 2 -> "HelloWorld123"
                it % 5 == 3 -> "손오반"
                it % 5 == 4 -> "초아이언99베지터"
                else -> ""
              },
              imogiCd = "EC010${it + 1}",
              buddyUserId = it,
              buddyType = "BR0101",
              buddyCnt = it,
              commentId = it,
              subCommentId = it,
              tradingId = it,
              noticeId = it,
              advertiseId = it,
              readYn = if (it > 3) "Y" else "N",
              commentContents = "",
              createdDate = when {
                it % 5 == 0 -> Date(System.currentTimeMillis() - 5 * 60 * 1000)
                it % 5 == 1 -> Date(System.currentTimeMillis() -3 * 60 * 60 * 1000)
                it % 5 == 2 -> Date(System.currentTimeMillis() -12 * 24 * 60 * 60 * 1000)
                it % 5 == 3 -> Date(System.currentTimeMillis() - 24 * 24 * 60 * 60 * 1000)
                it % 5 == 4 -> Date(System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000)
                else -> Date()
              },
              removeBuddy = "",
              tradingOwnerId = it,
            )
          },
          activityDto = List(5) {
            AlimCenterEntity.AlimActivityEntity(
              notiId = it,
              userId = it,
              contentsType = when {
                it % 5 == 0 -> "SUB_COMMENT"
                it % 5 == 1 -> "SUB_COMMENT"
                it % 5 == 2 -> "COMMENT"
                it % 5 == 3 -> "COMMENT"
                it % 5 == 4 -> "SUB_COMMENT"
                else -> ""
              },
              contents = when {
                it % 5 == 0 -> "삼성전자 내역에 댓글이 작성되었어요."
                it % 5 == 1 -> "대한항공 내역에 댓글이 작성되었어요."
                it % 5 == 2 -> "삼성전자 내역에 대한 매매노트를 작성했어요."
                it % 5 == 3 -> "대한항공 내역에 대한 매매노트를 작성했어요."
                it % 5 == 4 -> "삼성전자 내역에 댓글이 작성되었어요."
                else -> ""
              },
              nickname = when {
                it % 5 == 0 -> "둘리아빠"
                it % 5 == 1 -> "서에번쩍"
                it % 5 == 2 -> "HelloWorld123"
                it % 5 == 3 -> "손오반"
                it % 5 == 4 -> "초아이언99베지터"
                else -> ""
              },
              imogiCd = "EC010${it + 5}",
              buddyUserId = it,
              buddyType = "BR0101",
              buddyCnt = it,
              commentId = it,
              subCommentId = it,
              tradingId = it,
              noticeId = it,
              advertiseId = it,
              readYn = if (it > 3) "Y" else "N",
              commentContents =
                when {
                  it % 5 == 0 -> "와 이거 상승중인가요??"
                  it % 5 == 1 -> "존버중입니다 ㅋㅋㅋ"
                  it % 5 == 2 -> "최고점 찍었을때를 기록한다"
                  it % 5 == 3 -> "아 대한항공사니 여행가고싶네"
                  it % 5 == 4 -> "요즘 반도체 시장이 어떠신거같나요??"
                  else -> ""
                },
              createdDate = when {
                it % 5 == 0 -> Date(System.currentTimeMillis() - 5 * 60 * 1000)
                it % 5 == 1 -> Date(System.currentTimeMillis() -3 * 60 * 60 * 1000)
                it % 5 == 2 -> Date(System.currentTimeMillis() -12 * 24 * 60 * 60 * 1000)
                it % 5 == 3 -> Date(System.currentTimeMillis() - 24 * 24 * 60 * 60 * 1000)
                it % 5 == 4 -> Date(System.currentTimeMillis() - 90L * 24 * 60 * 60 * 1000)
                else -> Date()
              },
              removeBuddy = "",
              tradingOwnerId = it,
            )
          },
          moreNotice = false,
          moreTrading = true,
          moreActivity = true,
        )
      )
    }
  }

  override suspend fun getBuddyRequestFindAllItem(): ApiResponse<CommonApiResponse<BuddyRequestFindAllItemEntity>> {
    return ApiResponse.suspendOf {
      CommonApiResponse(
        code = 200,
        message = "success",
        data = BuddyRequestFindAllItemEntity(
            List(3) {
            BuddyRequestFindAllEntity(
              userId = it,
              buddyCnt = it,
              buddyRequestId = it,
              buddyType = "BR0101",
              channel = "",
              greetings = "",
              nickname = when {
                it % 3 == 0 -> "천진반"
                it % 3 == 1 -> "HelloKotlin"
                it % 3 == 2 -> "dev"
                else -> ""
              },
              phoneNum = "",
              readYN = "Y",
              userMail = "andrewjohnson@myownpersonaldomain.com",
              username = "",
              youtubeLink = "",
              imogiCd = "EC01${it + 10}",
              totalCnt = it,
              withBuddyCount = 3,
              withBuddyImogiCdList = List(3) { "EC010$it" },
//              requestDate = Date(),
              requestDate = when {
                it % 3 == 0 -> Date(System.currentTimeMillis() - 5 * 60 * 1000)
                it % 3 == 1 -> Date(System.currentTimeMillis() -3 * 60 * 60 * 1000)
                it % 3 == 2 -> Date(System.currentTimeMillis() -12 * 24 * 60 * 60 * 1000)
                else -> Date()
              },
            )
          },
          pageable = true,
          totalCnt = 10
        )
      )
    }
  }
}