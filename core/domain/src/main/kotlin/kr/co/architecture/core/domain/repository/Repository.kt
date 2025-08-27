package kr.co.architecture.core.domain.repository

import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.usecase.GetListUseCase

interface Repository {

  suspend fun searchBook(params: GetListUseCase.Params): SearchedBook

}