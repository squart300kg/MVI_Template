package kr.co.architecture.core.domain

import kr.co.architecture.core.domain.repository.ArticleRepository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
  private val repository: ArticleRepository
) {
  suspend operator fun invoke(): List<String> {
    return repository.getList()
      .map { it.name }
  }
}