package kr.co.architecture.core.datastore

import com.google.gson.Gson
import kr.co.architecture.core.datastore.model.StringMediaContentsEntity
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.model.MediaIdentity

data class MediaContentsEntity(
  val thumbnailUrl: String,
  val dateTime: String,
  val title: String,
  val collection: String? = null,
  val contents: String,
  val mediaContentsType: MediaContentsTypeEnum
) {
  companion object {
    fun mapperToEntity(entity: StringMediaContentsEntity): MediaContentsEntity =
      Gson().fromJson(entity.value, MediaContentsEntity::class.java)

    fun mapperToEntity(domain: MediaContents) = MediaContentsEntity(
      thumbnailUrl = domain.thumbnailUrl,
      dateTime = domain.dateTime,
      title = domain.title,
      collection = domain.collection,
      contents = domain.contents,
      mediaContentsType = domain.mediaContentsType
    )
    fun mapperToDomain(domain: MediaContentsEntity) = MediaContents(
      thumbnailUrl = domain.thumbnailUrl,
      dateTime = domain.dateTime,
      title = domain.title,
      collection = domain.collection,
      contents = domain.contents,
      mediaContentsType = domain.mediaContentsType
    )
  }
}

fun MediaContentsEntity.uniqueId(): String =
  MediaIdentity.idOf(mediaContentsType, title, contents, thumbnailUrl)
