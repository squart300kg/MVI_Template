package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.PicsumImagesDto

interface PicsumImageRepository {

  suspend fun getPicsumImages(page: Int): PicsumImagesDto

}