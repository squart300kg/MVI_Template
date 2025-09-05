package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.PicsumImagesDtoRequest
import kr.co.architecture.core.repository.dto.PicsumImagesDtoResponse

interface PicsumImageRepository {

  suspend fun getPicsumImages(dtoRequest: PicsumImagesDtoRequest): PicsumImagesDtoResponse

}