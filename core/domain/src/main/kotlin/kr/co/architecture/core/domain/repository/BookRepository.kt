package kr.co.architecture.core.domain.repository

import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.usecase.SearchBookUseCase

interface BookRepository {

  suspend fun searchBook(params: SearchBookUseCase.Params): SearchedBook

}