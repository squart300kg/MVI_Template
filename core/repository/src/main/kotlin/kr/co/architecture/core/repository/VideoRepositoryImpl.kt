package kr.co.architecture.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kr.co.architecture.core.datastore.LocalApi
import kr.co.architecture.core.datastore.MediaContentsEntity
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.operator.getOrThrowAppFailure
import kr.co.architecture.core.repository.dto.VideoDto
import kr.co.architecture.core.repository.dto.uniqueId
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
  private val remoteApi: RemoteApi,
  private val localApi: LocalApi
) : VideoRepository {

  private val cacheMutex = Mutex()
  private val cachedVideos = LinkedHashMap<String, VideoDto.Video>()

  override fun observeBookmarkedMedias(): Flow<Set<MediaContents>> =
    localApi.observeBookmarkedMedias()
      .map { entities ->
        entities.map { entity ->
          MediaContentsEntity.mapperToDomain(entity)
        }.toSet()
      }

  override suspend fun getVideos(query: ContentsQuery): VideoDto {
    return remoteApi.getVideos(
      query = query.query,
      page = query.page
    )
      .getOrThrowAppFailure()
      .let(VideoDto::mapperToDto)
      .also { videoDto ->
        cacheMutex.withLock {
          videoDto.videos.forEach { video ->
            cachedVideos[video.uniqueId()] = video
          }
        }
      }
  }

  override suspend fun searchVideo(id: String): VideoDto.Video? =
    cacheMutex.withLock { cachedVideos[id] }

  override suspend fun toggleBookmark(contents: MediaContents, toggleType: ToggleTypeEnum) {
    val entity = MediaContentsEntity.mapperToEntity(contents)
    when (toggleType) {
      ToggleTypeEnum.SAVE -> localApi.upsert(entity)
      ToggleTypeEnum.DELETE -> localApi.delete(entity)
    }
  }

}


