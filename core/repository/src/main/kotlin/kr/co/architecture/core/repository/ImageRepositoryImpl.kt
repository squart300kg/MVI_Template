package kr.co.architecture.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kr.co.architecture.core.datastore.LocalApi
import kr.co.architecture.core.datastore.MediaContentsEntity
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.ImageDto
import kr.co.architecture.core.repository.dto.VideoDto
import kr.co.architecture.core.repository.dto.uniqueId
import javax.inject.Inject
import kotlin.collections.set

class ImageRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi,
  private val localApi: LocalApi
) : ImageRepository {

  private val cacheMutex = Mutex()
  private val cachedImages = LinkedHashMap<String, ImageDto.Image>()

  override fun observeBookmarkedMedias(): Flow<Set<MediaContents>> =
    localApi.observeBookmarkedMedias()
      .map { entities ->
        entities.map { entity ->
          MediaContentsEntity.mapperToDomain(entity)
        }.toSet()
      }

  override suspend fun getImages(query: ContentsQuery): ImageDto {
    return remoteApi.getImages(
      query = query.query,
      page = query.page
    )
      .getOrThrowAppFailure()
      .let(ImageDto::mapperToDto)
      .also { imageDto ->
        cacheMutex.withLock {
          imageDto.images.forEach { image ->
            cachedImages[image.uniqueId()] = image
          }
        }
      }
  }

  override suspend fun searchImages(id: String): ImageDto.Image? =
    cacheMutex.withLock { cachedImages[id] }

  override suspend fun toggleBookmark(contents: MediaContents, toggleType: ToggleTypeEnum) {
    val entity = MediaContentsEntity.mapperToEntity(contents)
    when (toggleType) {
      ToggleTypeEnum.SAVE -> localApi.upsert(entity)
      ToggleTypeEnum.DELETE -> localApi.delete(entity)
    }
  }
}
