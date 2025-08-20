package kr.co.architecture.core.network

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.core.network.model.AlimCenterEntity
import kr.co.architecture.core.network.model.BuddyRequestFindAllItemEntity
import kr.co.architecture.core.network.operator.CommonApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteApi {

  suspend fun getAlimList(): ApiResponse<CommonApiResponse<AlimCenterEntity>>

  suspend fun getBuddyRequestFindAllItem(): ApiResponse<CommonApiResponse<BuddyRequestFindAllItemEntity>>

}