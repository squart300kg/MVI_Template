package kr.co.architecture.core.network.operator

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrThrow
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.raw
import com.skydoves.sandwich.retrofit.statusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kr.co.architecture.core.model.ApiException


suspend fun <ENTITY> ApiResponse<CommonApiResponse<ENTITY>>.safeGet(): ENTITY? = this
  .suspendOnSuccess {
    if (data.code != 200) {
      throw ApiException.ApiOnError(
        code = this.data.code,
        message = this.data.message
      )
    }
  }
  .suspendOnError {
    throw ApiException.ApiOnError(
      code = this.statusCode.code,
      message = this.message()
    )
  }
  .suspendOnException {
    throw ApiException.ApiOnException(
      throwable = this.throwable,
      message = this.message ?: "Unknown Error"
    )
  }
  .getOrThrow()
  .data

// 소수 API에서, http code가 200인데도 불구, CommonEntity의 `data`가 null인경우 존재.
// DataLayer의 인터페이스를 NotNull하게 만들기 위한 함수.
fun <FIELD> FIELD?.throwIfNull(): FIELD {
  return requireNotNull(this) { "`data` field is null." }
}