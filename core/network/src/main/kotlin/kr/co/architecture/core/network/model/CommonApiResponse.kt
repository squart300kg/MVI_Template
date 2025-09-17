package kr.co.architecture.core.network.model

import kotlinx.serialization.SerialName

data class CommonApiResponse<out T>(
  @SerialName("documents")
  val documents: List<T>,
  @SerialName("meta")
  val meta: Meta
)