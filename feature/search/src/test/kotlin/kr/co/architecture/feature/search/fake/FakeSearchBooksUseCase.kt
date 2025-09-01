package kr.co.architecture.feature.search.fake

import kr.co.architecture.core.domain.entity.Pageable
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase

class FakeSearchBooksUseCase : SearchBooksUseCase {
  var lastParams: SearchBooksUseCase.Params? = null
  var result: SearchedBooks = SearchedBooks(books = emptyList(), pageable = Pageable(isEnd = true, pageableCount = 10))

  override suspend fun invoke(params: SearchBooksUseCase.Params): SearchedBooks {
    lastParams = params
    return result
  }
}