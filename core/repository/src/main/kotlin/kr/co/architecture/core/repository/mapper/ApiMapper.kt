package kr.co.architecture.core.repository.mapper

import com.google.gson.Gson
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrThrow
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import kr.co.architecture.core.domain.entity.DomainFailure
import kr.co.architecture.core.network.error.KakaoErrorApiResponse
import java.net.UnknownHostException

suspend fun <ENTITY> ApiResponse<ENTITY>.getOrThrowDomainFailure(): ENTITY = this
  .suspendOnError {
    throw try {
      /**
       * Kakao API의 error case맞게 모델 파싱

       * 실무 진행 시엔, 실제 발생 가능한 error case를 해당 블럭에서 정의 및 분기처리
       */
      Gson().fromJson(errorBody?.string(), KakaoErrorApiResponse::class.java)
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
      is KakaoErrorApiResponse -> DomainFailure.Error(
        code = e.errorType,
        message = e.message
      )
      is UnknownHostException -> DomainFailure.Exception.NetworkConnection
      else -> DomainFailure.Exception.Unknown
    }
  }
  .getOrThrow()

