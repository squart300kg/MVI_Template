package kr.co.architecture.core.network.model

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
  @SerializedName("author")
  val author: String?,
  @SerializedName("content")
  val content: String?,
  @SerializedName("description")
  val description: String?,
  @SerializedName("publishedAt")
  val publishedAt: String,
  @SerializedName("source")
  val source: Source,
  @SerializedName("title")
  val title: String,
  @SerializedName("url")
  val url: String,
  @SerializedName("urlToImage")
  val urlToImage: String?
) {
  data class Source(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
  )
}