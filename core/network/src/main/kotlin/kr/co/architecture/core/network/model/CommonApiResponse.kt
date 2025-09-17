package kr.co.architecture.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class CommonApiResponse<out T>(
  @SerializedName("documents")
  val documents: List<T>,
  @SerializedName("meta")
  val meta: Meta
)