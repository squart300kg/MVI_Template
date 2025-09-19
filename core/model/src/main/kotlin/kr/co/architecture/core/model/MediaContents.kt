package kr.co.architecture.core.model

data class MediaContents(
  val thumbnailUrl: String,
  val dateTime: String,
  val title: String,
  val collection: String? = null,
  val contents: String,
  val mediaContentsType: MediaContentsTypeEnum
)

// TODO: id의 값을 inline class로 두기
fun MediaContents.uniqueId(): String =
  MediaIdentity.idOf(
    mediaContentsType = mediaContentsType,
    title = title,
    contents = contents,
    thumbnailUrl = thumbnailUrl
  )