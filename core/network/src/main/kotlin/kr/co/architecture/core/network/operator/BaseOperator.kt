package kr.co.architecture.core.network.operator

import com.google.gson.Gson
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrThrow
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import kr.co.architecture.core.network.error.KakaoErrorApiResponse

suspend fun <ENTITY> ApiResponse<ENTITY>.executeWithDomain(): ENTITY = this
  .suspendOnError {
    throw try {
      payload
      Gson().fromJson(errorBody?.string(), KakaoErrorApiResponse::class.java)
    } catch (e: Exception) { throw e }
  }
  .suspendOnException {
    /**
     * API의 request / response가 정상적으로 수행되지 않는 경우
     * 지금 이 블럭에서 예외를 던짐(eg., SSLHandshakeException, UnknownHostException...)
     */
    throw throwable
  }
  .getOrThrow()

