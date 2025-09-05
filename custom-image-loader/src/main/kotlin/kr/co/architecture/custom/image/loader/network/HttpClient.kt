package kr.co.architecture.custom.image.loader.network

import kr.co.architecture.custom.http.client.model.Bytes

interface HttpClient {
  suspend fun get(url: String, header: Map<String, String> = emptyMap()): Response

  data class Response(
    val code: Int,
    val header: Map<String, String>,
    val body: Bytes?
  )
}
