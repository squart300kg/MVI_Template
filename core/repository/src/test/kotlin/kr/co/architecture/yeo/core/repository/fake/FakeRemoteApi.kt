package kr.co.architecture.yeo.core.repository.fake

import com.skydoves.sandwich.ApiResponse
import kr.co.architecture.yeo.core.network.RemoteApi
import kr.co.architecture.yeo.core.network.model.SearchedBookApiResponse

class FakeRemoteApi : RemoteApi {
  private val queue = ArrayDeque<ApiResponse<SearchedBookApiResponse>>()

  fun enqueue(apiResponse: ApiResponse.Success<SearchedBookApiResponse>) {
    queue.add(apiResponse)
  }

  override suspend fun searchBook(
    query: String,
    sort: String,
    page: Int,
    size: Int,
    target: Int?
  ): ApiResponse<SearchedBookApiResponse> {
    return checkNotNull(queue.removeFirstOrNull()) { "FakeRemoteApi.enqueue() 먼저 호출하세요." }
  }
}