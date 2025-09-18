package kr.co.architecture.core.model

data class MediaContents(
  val thumbnailUrl: String,
  val dateTime: String,
  val title: String,
  val collection: String? = null,
  val contents: String,
  val mediaContentsType: MediaContentsTypeEnum
)

fun MediaContents.uniqueId(): String =
  MediaIdentity.idOf(mediaContentsType, title, contents, thumbnailUrl)