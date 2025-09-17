package kr.co.architecture.core.network.model

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("author")
    val author: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("play_time")
    val playTime: Int,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)