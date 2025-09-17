package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.ImageDto

interface ImageRepository {

  suspend fun getImages(): ImageDto

}