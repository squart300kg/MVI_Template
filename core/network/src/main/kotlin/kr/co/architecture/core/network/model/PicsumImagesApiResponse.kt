package kr.co.architecture.core.network.model

import com.google.gson.annotations.SerializedName
import kr.co.architecture.core.network.model.PicsumImagesApiResponse.PicsumImagesApiResponseItem

class PicsumImagesApiResponse : ArrayList<PicsumImagesApiResponseItem>() {
  data class PicsumImagesApiResponseItem(
    @SerializedName("author")
    val author: String,
    @SerializedName("download_url")
    val downloadUrl: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String
  )
}