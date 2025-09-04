package kr.co.architecture.custom.image.loader.network

interface HttpClient {
  suspend fun get(url: String, header: Map<String, String> = emptyMap()): Response

  data class Response(
    val code: Int,
    val header: Map<String, String>,
    val body: ByteArray?
  )
}
