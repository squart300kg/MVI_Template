package kr.co.architecture.core.network

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.core.network.model.AlimCenterEntity
import kr.co.architecture.core.network.model.AlimCenterEntity.AlimNoticeEntity
import kr.co.architecture.core.network.model.BuddyRequestFindAllItemEntity
import kr.co.architecture.core.network.operator.CommonApiResponse
import javax.inject.Inject

class RemoteMockApiImpl @Inject constructor(

) : RemoteApi {
  override suspend fun getAlimList(): ApiResponse<CommonApiResponse<AlimCenterEntity>> {
    return ApiResponse.suspendOf {
      CommonApiResponse(
        code = 200,
        message = "success",
        data = AlimCenterEntity(
          noticeDto = emptyList<AlimNoticeEntity>(),
          tradingDto = emptyList<AlimCenterEntity.AlimTradingEntity>(),
          activityDto = emptyList<AlimCenterEntity.AlimActivityEntity>(),
          moreNotice = true,
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
          list = emptyList(),
          pageable = true,
          totalCnt = 10
        )
      )
    }
  }
}