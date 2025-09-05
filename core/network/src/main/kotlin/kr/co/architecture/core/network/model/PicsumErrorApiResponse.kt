package kr.co.architecture.core.network.model

import com.google.gson.Gson
import java.nio.charset.Charset

data class PicsumErrorApiResponse(
  val code: String,
  override val message: String
) : Exception(message) {
  companion object {
    fun mapperToApiResponse(byteArray: ByteArray) = Gson().fromJson(
      byteArray.toString(Charset.forName("UTF-8")),
      PicsumErrorApiResponse::class.java
    )
  }
}