package kr.co.architecture.core.repository

import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.repository.dto.VideoDto

interface VideoRepository {

  suspend fun getVideos(query: ContentsQuery): VideoDto

  suspend fun toggleBookmark(contents: MediaContents, toggleType: ToggleTypeEnum)
}