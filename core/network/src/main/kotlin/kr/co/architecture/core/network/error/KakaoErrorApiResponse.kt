package kr.co.architecture.core.network.error

import com.google.gson.annotations.SerializedName

data class KakaoErrorApiResponse(
  @SerializedName("errorType")
  val errorType: String,
  @SerializedName("message")
  override val message: String
) : Exception(message)
