package kr.co.architecture.core.repository.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kr.co.architecture.core.database.dao.BookSearchDao
import kr.co.architecture.core.database.entity.BookEntity

class FakeBookSearchDao : BookSearchDao {
  private val state = MutableStateFlow<List<BookEntity>>(emptyList())

  override fun observeBookmarkedBooks(): Flow<List<BookEntity>> = state
  fun emit(book: BookEntity) {
    state.update { state.value + book }
  }

  override suspend fun delete(isbn: String) {
    state.update { it.filterNot { e -> e.isbn == isbn } }
  }

  override suspend fun upsert(vararg bookEntity: BookEntity) {
    val map = state.value.associateBy { it.isbn }.toMutableMap()
    bookEntity.forEach { map[it.isbn] = it }
    state.value = map.values.toList()
  }
}