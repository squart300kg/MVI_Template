package kr.co.architecture.custom.image.loader.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kr.co.architecture.custom.http.client.constants.HttpHeaderConstants.Method.GET
import kr.co.architecture.custom.http.client.RawHttp11Client

class HttpClientImpl private constructor(
  private val client: RawHttp11Client
) : HttpClient {

  companion object {
    @Volatile
    private var INSTANCE: HttpClient? = null

    @JvmStatic
    fun getInstance(
      client: RawHttp11Client
    ): HttpClient {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: HttpClientImpl(
          client = client
        ).also { INSTANCE = it }
      }
    }
  }

  override suspend fun get(url: String, header: Map<String, String>): HttpClient.Response {
    return callbackFlow {
      client.callApi(
        method = GET,
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
        onResponseError = {
          send(
            HttpClient.Response(
              code = this.code,
              header = this.header,
              body = this.body
            )
          )
        },
        onResponseException = { throw this }
      )

      awaitClose()
    }.first()
  }
}
