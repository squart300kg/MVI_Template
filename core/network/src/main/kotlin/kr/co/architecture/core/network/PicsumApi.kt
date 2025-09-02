package kr.co.architecture.core.network

import kr.co.architecture.core.network.httpClient.PageResult

interface PicsumApi {

  suspend fun getPicsumImages(path: String, page: Int, limit: Int): PageResult

}