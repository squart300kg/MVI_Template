package kr.co.architecture.core.network.model

import kotlinx.serialization.SerialName

data class Video(
    @SerialName("author")
    val author: String,
    @SerialName("datetime")
    val datetime: String,
    @SerialName("play_time")
    val playTime: Int,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String
)