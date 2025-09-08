package kr.co.architecture.core.network.operator

import kr.co.architecture.core.model.ArchitectureSampleHttpFailure
import kr.co.architecture.core.network.model.ApiResponse
import kr.co.architecture.core.network.model.PicsumErrorApiResponse
import kr.co.architecture.core.network.model.PicsumImagesApiResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun ApiResponse<PicsumImagesApiResponse, PicsumErrorApiResponse>.getOrThrowAppFailure(): ApiResponse.Success<PicsumImagesApiResponse> =
  when (this) {
    is ApiResponse.Success -> this
    is ApiResponse.Error -> {
      /**
       * API의 error case맞게 모델 파싱
       * 실무 진행 시엔, 실제 발생 가능한 error case를 해당 블럭에서 정의 및 분기처리
       */
      throw this.data
    }
    is ApiResponse.Exception -> {
      /**
       * API의 request / response가 정상적으로 수행되지 않는 경우
       * 지금 이 블럭에서 예외를 던짐(eg., `SSLHandshakeException`, `UnknownHostException`...)
       *
       * 실무 진행 시엔, 실제 발생 가능한 exception case를 해당 블럭에서 정의 및 분기처리
       */
      throw when (throwable) {
        is UnknownHostException -> ArchitectureSampleHttpFailure.Exception.NetworkConnection
        is SocketTimeoutException -> ArchitectureSampleHttpFailure.Exception.SocketTimeout
        else -> ArchitectureSampleHttpFailure.Exception.Unknown
      }
    }
  }