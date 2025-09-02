package kr.co.architecture.core.network.model

import com.google.gson.annotations.SerializedName

data class PicsumErrorApiResponse(
  val code: String,
  override val message: String
) : Exception(message)