package kr.co.architecture.core.network.model

import kotlinx.serialization.SerialName

data class Image(
    @SerialName("collection")
    val collection: String,
    @SerialName("datetime")
    val datetime: String,
    @SerialName("display_sitename")
    val displaySitename: String,
    @SerialName("doc_url")
    val docUrl: String,
    @SerialName("height")
    val height: Int,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    @SerialName("width")
    val width: Int
)