package kr.co.architecture.core.datastore

import kotlinx.coroutines.flow.Flow

interface LocalApi {

  fun observeBookmarkedBooks(): Flow<Set<MediaContentsEntity>>

  suspend fun delete(entity: MediaContentsEntity)

  suspend fun upsert(entity: MediaContentsEntity)
}