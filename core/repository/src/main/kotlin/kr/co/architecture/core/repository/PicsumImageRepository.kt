package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.PicsumImageDto

interface PicsumImageRepository {

  suspend fun getPicsumImages(): List<PicsumImageDto>

}