package kr.co.architecture.core.repository

import kr.co.architecture.core.repository.dto.AlimListRequestDto
import kr.co.architecture.core.repository.dto.AlimListResponseDto

interface AlimRepository {

  suspend fun getAlimList(dto: AlimListRequestDto): AlimListResponseDto

}