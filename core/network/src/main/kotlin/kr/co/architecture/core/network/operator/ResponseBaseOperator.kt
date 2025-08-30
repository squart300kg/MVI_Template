package kr.co.architecture.core.network.operator

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.getOrThrow
import com.skydoves.sandwich.retrofit.raw
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import kr.co.architecture.core.model.ArchitectureSampleHttpException

// TODO: OnException, onError의 처리는 UseCase가 맞음
suspend fun <ENTITY> ApiResponse<ENTITY>.safeGet(): ENTITY = this
  .suspendOnError {
    throw ArchitectureSampleHttpException(
      code = raw.code,
      message = raw.message,
    )
  }
  .suspendOnException { throw this.throwable }
  .getOrThrow()

