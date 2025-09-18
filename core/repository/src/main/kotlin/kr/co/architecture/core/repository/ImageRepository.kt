package kr.co.architecture.core.repository

import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.repository.dto.ImageDto

interface ImageRepository {

  suspend fun getImages(query: ContentsQuery): ImageDto

  suspend fun toggleBookmark(contents: MediaContents, toggleType: ToggleTypeEnum)
}