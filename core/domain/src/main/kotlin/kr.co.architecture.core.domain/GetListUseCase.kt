package kr.co.architecture.core.domain

import kr.co.architecture.core.repository.ImageRepository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
  private val imageRepository: ImageRepository
) {
  suspend operator fun invoke(): List<String> {
    return listOf("sdf", "sdf", "sdf", "sdf")
  }
}