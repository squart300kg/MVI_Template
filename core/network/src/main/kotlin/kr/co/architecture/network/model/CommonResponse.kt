package kr.co.architecture.network.model

import com.google.gson.annotations.SerializedName

data class CommonResponse<out T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("articles")
    val articles: List<T>,
)