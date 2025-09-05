package kr.co.architecture.custom.http.client.model

data class HttpResponse(
  val code: Int,
  val message: String,
  val header: Map<String, String> = emptyMap(),
  val body: Bytes = Bytes(byteArrayOf())
)