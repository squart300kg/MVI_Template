package kr.co.architecture.core.repository.dto

import kr.co.architecture.core.model.enums.AlimCenterTypeEnum

data class AlimListRequestDto(
  val size: Int,
  val page: Int,
  val type: AlimCenterTypeEnum
)