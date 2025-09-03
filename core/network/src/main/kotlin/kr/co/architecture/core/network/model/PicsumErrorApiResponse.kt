package kr.co.architecture.core.network.model

data class PicsumErrorApiResponse(
  val code: String,
  override val message: String
) : Exception(message)