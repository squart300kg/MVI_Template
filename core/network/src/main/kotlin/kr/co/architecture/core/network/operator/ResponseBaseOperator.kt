package kr.co.architecture.core.network.operator

import com.google.gson.Gson
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrThrow
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.raw
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import kr.co.architecture.core.model.ArchitectureSampleHttpFailure
import kr.co.architecture.core.network.model.CommonErrorResponse
import kr.co.architecture.core.network.model.CommonResponse
import java.net.UnknownHostException


suspend fun <ENTITY> ApiResponse<CommonResponse<ENTITY>>.safeGet(): List<ENTITY> = this
  .suspendOnError {
    throw try {
      /**
       * API의 error case맞게 모델 파싱
       * 실무 진행 시엔, 실제 발생 가능한 error case를 해당 블럭에서 정의 및 분기처리
       */
      Gson().fromJson(errorBody?.string(), CommonErrorResponse::class.java)
    } catch (e: Exception) { throw e }
  }
  .suspendOnException {
    /**
     * API의 request / response가 정상적으로 수행되지 않는 경우
     * 지금 이 블럭에서 예외를 던짐(eg., `SSLHandshakeException`, `UnknownHostException`...)
     *
     * 실무 진행 시엔, 실제 발생 가능한 exception case를 해당 블럭에서 정의 및 분기처리
     */
    throw when (val e = throwable) {
      is CommonErrorResponse -> ArchitectureSampleHttpFailure.Error(
        code = e.status,
        message = e.message
      )
      is UnknownHostException -> ArchitectureSampleHttpFailure.Exception.NetworkConnection
      else -> ArchitectureSampleHttpFailure.Exception.Unknown
    }
  }
  .getOrThrow()
  .articles