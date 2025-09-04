package kr.co.architecture.custom.image.loader.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kr.co.architecture.custom.http.client.RawHttp11Client

interface HttpClient {
  suspend fun get(url: String, header: Map<String, String> = emptyMap()): Response

  data class Response(
    val code: Int,
    val header: Map<String, String>,
    val body: ByteArray?
  )
}

class RawHttp11ClientAdapter(
  private val client: RawHttp11Client
) : HttpClient {

  override suspend fun get(url: String, header: Map<String, String>): HttpClient.Response {
    return callbackFlow {
      client.callApi(
        method = "GET",
        url = url,
        headers = header.toMutableMap(),
        onResponseSuccess = {
          send(
            HttpClient.Response(
              code = code,
              header = this.header.mapKeys { it.key.lowercase(java.util.Locale.ROOT) },
              body = body
            )
          )
        },
        onResponseError = { send(HttpClient.Response(code, emptyMap(), null)) },
        onResponseException = { throw this }
      )

      awaitClose()
    }.first()
  }
}
