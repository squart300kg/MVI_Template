package kr.co.architecture.network.model

import com.google.gson.annotations.SerializedName

data class CommonErrorResponse<out T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("message")
    val message: T,
)