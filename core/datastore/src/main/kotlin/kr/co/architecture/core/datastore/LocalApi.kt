package kr.co.architecture.core.datastore

import kotlinx.coroutines.flow.Flow

interface LocalApi {

  fun observeBookmarkedMedias(): Flow<Set<MediaContentsEntity>>

  suspend fun delete(entity: MediaContentsEntity)

  suspend fun upsert(entity: MediaContentsEntity)
}