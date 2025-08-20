package kr.co.architecture.core.domain

import kr.co.architecture.core.repository.Repository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
  private val repository: Repository
) {
  suspend operator fun invoke(): List<String> {
    return repository.getList()
      .map { it.name }
  }
}