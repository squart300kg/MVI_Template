package kr.co.architecture.core.network.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kr.co.architecture.core.network.model.PicsumImagesApiResponse.PicsumImagesApiResponseItem
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.AUTHOR
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.DOWNLOAD_URL
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.HEIGHT
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.WIDTH
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.ID
import kr.co.architecture.core.network.model.PicsumImagesApiResponseField.URL
import java.nio.charset.Charset

class PicsumImagesApiResponse : ArrayList<PicsumImagesApiResponseItem>() {
  data class PicsumImagesApiResponseItem(
    @SerializedName(AUTHOR)
    val author: String,
    @SerializedName(DOWNLOAD_URL)
    val downloadUrl: String,
    @SerializedName(WIDTH)
    val width: Int,
    @SerializedName(HEIGHT)
    val height: Int,
    @SerializedName(ID)
    val id: String,
    @SerializedName(URL)
    val url: String
  )
  companion object {
    fun mapperToApiResponse(byteArray: ByteArray) = Gson().fromJson(
      byteArray.toString(Charset.forName("UTF-8")),
      PicsumImagesApiResponse::class.java
    )
  }
}

object PicsumImagesApiResponseField {
  const val AUTHOR = "author"
  const val DOWNLOAD_URL = "download_url"
  const val WIDTH = "width"
  const val HEIGHT = "height"
  const val ID = "id"
  const val URL = "url"
}

object PicsumImagesApiHeaderField {
  const val LINK = "link"
}