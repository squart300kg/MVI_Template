package kr.co.architecture.core.repository

import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.repository.dto.VideoDto

interface VideoRepository {

  fun observeBookmarkedMedias(): Flow<Set<MediaContents>>

  suspend fun getVideos(query: ContentsQuery): VideoDto

  suspend fun searchVideo(id: String): VideoDto.Video?

  suspend fun toggleBookmark(contents: MediaContents, toggleType: ToggleTypeEnum)
}