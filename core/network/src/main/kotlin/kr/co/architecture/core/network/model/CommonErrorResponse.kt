package kr.co.architecture.core.network.model

import com.google.gson.annotations.SerializedName

data class CommonErrorResponse(
  @SerializedName("status")
  val status: String,
  @SerializedName("totalResults")
  val totalResults: Int,
  @SerializedName("message")
  override val message: String,
) : Exception(message)