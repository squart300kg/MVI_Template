package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.VideoDto

interface VideoRepository {

  suspend fun getVideos(): VideoDto

}