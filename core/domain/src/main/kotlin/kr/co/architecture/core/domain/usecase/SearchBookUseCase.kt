package kr.co.architecture.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kr.co.architecture.core.domain.entity.DomainResult
import kr.co.architecture.core.domain.entity.SearchedBook
import kr.co.architecture.core.domain.enums.SearchTypeEnum
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(
  private val bookRepository: BookRepository
) {
  @OptIn(ExperimentalCoroutinesApi::class)
  operator fun invoke(params: Params): Flow<DomainResult<SearchedBook>> = flow {
    emit(DomainResult.Loading)
    emitAll(
      combine(
        flow = flowOf(bookRepository.searchBook(params)),
        flow2 = bookRepository.observeBookmarkedBooks()
      ) { searched, bookmarked ->

        val merged = searched.copy(
          books = searched.books.map { book ->
            book.copy(isBookmarked = bookmarked.any { it.isbn == book.isbn })
          }
        )
        DomainResult.Success(merged)
      }
    )
  }.catch { emit(DomainResult.Error(it)) }

  data class Params(
    val page: Int,
    val query: String,
    val sortTypeEnum: SortTypeEnum,
    val searchTypeEnum: SearchTypeEnum
  )
}