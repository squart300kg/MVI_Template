package kr.co.architecture.core.network.operator

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrThrow
import com.skydoves.sandwich.operators.ApiResponseSuspendOperator
import com.skydoves.sandwich.retrofit.raw
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.sandwich.suspendOperator
import kr.co.architecture.core.model.ArchitectureSampleHttpException
import kr.co.architecture.core.network.model.CommonResponse


suspend fun <ENTITY> ApiResponse<CommonResponse<ENTITY>>.safeGet(): List<ENTITY> = this
  .suspendOnSuccess {
    if (raw.code == 401) {
      throw ArchitectureSampleHttpException(
        code = data.totalResults,
        message = raw.message,
      )
    }
  }
  .suspendOnError {
    throw ArchitectureSampleHttpException(
      code = raw.code,
      message = raw.message,
    )
  }
  .suspendOnException { throw this.throwable }
  .getOrThrow()
  .articles