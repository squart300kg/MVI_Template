package kr.co.architecture.core.network.operator

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.operators.ApiResponseSuspendOperator
import com.skydoves.sandwich.retrofit.raw
import kr.co.architecture.core.model.ArchitectureSampleHttpException
import kr.co.architecture.core.network.model.CommonResponse

class ResponseBaseOperator<ENTITY, DTO>(
    private val onSuccess: suspend (data: DTO) -> Unit,
    private val mapper: ((List<ENTITY>) -> DTO),
) : ApiResponseSuspendOperator<CommonResponse<ENTITY>>() {

    override suspend fun onSuccess(apiResponse: ApiResponse.Success<CommonResponse<ENTITY>>) {
        if (apiResponse.raw.code != 401) {
            onSuccess(mapper.invoke(apiResponse.data.articles))
        } else {
            throw ArchitectureSampleHttpException(
                code = apiResponse.data.totalResults,
                message = apiResponse.raw.message,
            )
        }
    }

    override suspend fun onError(apiResponse: ApiResponse.Failure.Error) {
        throw ArchitectureSampleHttpException(
            code = apiResponse.raw.code,
            message = apiResponse.raw.message,
        )
    }

    override suspend fun onException(apiResponse: ApiResponse.Failure.Exception) {
        throw apiResponse.throwable
    }
}