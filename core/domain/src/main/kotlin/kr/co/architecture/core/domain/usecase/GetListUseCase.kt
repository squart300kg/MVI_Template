package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.repository.Repository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
  private val repository: Repository
) {
  suspend operator fun invoke(): List<String> {
    return repository.getList()
      .map { it.name }
  }
}