package kr.co.architecture.core.domain.usecase

import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.repository.Repository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
  private val repository: Repository
) {
  suspend operator fun invoke(params: Params): SearchedBook {
    return repository.searchBook(params)
  }

  data class Params(
    val page: Int,
    val query: String,
    val sortTypeEnum: SortTypeEnum
  )
}