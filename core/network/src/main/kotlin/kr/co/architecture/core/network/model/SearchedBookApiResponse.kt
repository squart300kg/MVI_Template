package kr.co.architecture.core.network.model

import com.google.gson.annotations.SerializedName
import java.util.Date

const val NO_EXIST_PRICE = -1

data class SearchedBookApiResponse(
  @SerializedName("documents")
  val documents: List<Book>,
  @SerializedName("meta")
  val meta: Meta
) {
  data class Book(
    @SerializedName("authors")
    val authors: List<String>,
    @SerializedName("contents")
    val contents: String,
    @SerializedName("datetime")
    val dateTime: Date,
    @SerializedName("isbn")
    val isbn: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("publisher")
    val publisher: String,
    @SerializedName("sale_price")
    val salePrice: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("translators")
    val translators: List<String>,
    @SerializedName("url")
    val url: String
  )
  data class Meta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
  )
}