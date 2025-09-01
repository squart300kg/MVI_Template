@file:OptIn(ExperimentalCoroutinesApi::class)
package kr.co.architecture.core.repository

import com.skydoves.sandwich.ApiResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.enums.SortEnum
import kr.co.architecture.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.core.network.model.SearchedBookApiResponse
import kr.co.architecture.core.network.model.SearchedBookApiResponse.Meta
import kr.co.architecture.core.repository.fake.FakeBookSearchDao
import kr.co.architecture.core.repository.fake.FakeRemoteApi
import kr.co.architecture.core.repository.mapper.BookMapper
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookRepositoryImplTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

  private lateinit var remoteApi: FakeRemoteApi
  private lateinit var bookSearchDao: FakeBookSearchDao
  private lateinit var repository: BookRepositoryImpl

  @Before
  fun setup() {
    remoteApi = FakeRemoteApi()
    bookSearchDao = FakeBookSearchDao()
    repository = BookRepositoryImpl(
      remoteApi = remoteApi, bookSearchDao = bookSearchDao)
  }

  @Test
  fun toggleBookmark_호출시_observeBookmarkedBooks_스트림이_변경을_방출한다() = runTest {
    // GIVEN
    val actualBook = book(2)
    remoteApi.enqueue(apiSuccess(actualBook))
    repository.searchBooks(
      params = SearchBooksUseCase.Params(
        page = 1, query = "Q", sortEnum = SortEnum.ACCURACY
      )
    )

    val emissions = mutableListOf<List<Book>>()
    launch(UnconfinedTestDispatcher(testScheduler)) {
      repository.observeBookmarkedBooks().take(2).toList(emissions)
    }

    // WHEN
    repository.toggleBookmark(
      ToggleBookmarkUseCase.Params(
        bookmarkToggleTypeEnum = BookmarkToggleTypeEnum.SAVE,
        isbn = ISBN(actualBook.isbn)
      )
    )

    // THEN
    val expectedBook = emissions[1].find { it.isbn == actualBook.isbn }
    assertEquals(actualBook.isbn, expectedBook?.isbn)
    assertEquals(true, expectedBook?.isBookmarked)
  }

  @Test
  fun 검색API_호출한_상황에서_toggleBookmark_SAVE_이후엔_searchBook를_호출하면_북마크_true를_반영해_반환한다() = runTest {
    // GIVEN
    val book = book(2)
    remoteApi.enqueue(apiSuccess(book))
    repository.searchBooks(
      params = SearchBooksUseCase.Params(
        page = 1,
        query = "Q",
        sortEnum = SortEnum.ACCURACY)
    )

    // WHEN
    repository.toggleBookmark(
      ToggleBookmarkUseCase.Params(
        bookmarkToggleTypeEnum = BookmarkToggleTypeEnum.SAVE,
        isbn = ISBN(book.isbn)
      )
    )

    // THEN
    val searchedBook = repository.searchBook(SearchBookUseCase.Params(ISBN(book.isbn)))
    assertNotNull(searchedBook)
    assertEquals(book.isbn, searchedBook?.isbn)
    assertTrue(searchedBook?.isBookmarked == true)
  }

  @Test
  fun 캐싱된Book_조회한_상황에서_toggleBookmark_SAVE_이후엔_searchBook를_호출하면_북마크_true를_반영해_반환한다() = runTest {
    // GIVEN
    val book = BookMapper
      .mapperToDomain(book(2))
      .let(BookMapper::mapperToEntity)
    bookSearchDao.emit(book)

    // WHEN
    repository.toggleBookmark(
      ToggleBookmarkUseCase.Params(
        bookmarkToggleTypeEnum = BookmarkToggleTypeEnum.SAVE,
        isbn = ISBN(book.isbn)
      )
    )

    // THEN
    val searchedBook = repository.searchBook(SearchBookUseCase.Params(ISBN(book.isbn)))
    assertNotNull(searchedBook)
    assertEquals(book.isbn, searchedBook?.isbn)
    assertTrue(searchedBook?.isBookmarked == true)
  }

  private fun book(
    i: Int,
    title: String = "제목 $i",
    authors: List<String> = listOf("저자 $i"),
    publisher: String = "출판사 $i",
    dateTime: String = "2021-01-${"%02d".format(i)}T00:00:00.000+09:00",
    originPrice: Int = i * 1_000,
    salePrice: Int = -1, // -1이면 할인 없음
    isbn: String = "isbn$i"
  ) = SearchedBookApiResponse.Book(
    authors = authors,
    contents = "설명 $i",
    dateTime = dateTime,
    isbn = isbn,
    price = originPrice,
    publisher = publisher,
    salePrice = salePrice,
    status = "정상",
    thumbnail = "",
    title = title,
    translators = emptyList(),
    url = "https://example.com/$i"
  )

  private fun apiSuccess(
    vararg docs: SearchedBookApiResponse.Book,
    isEnd: Boolean = false
  ): ApiResponse.Success<SearchedBookApiResponse> =
    ApiResponse.Success(
      SearchedBookApiResponse(
        documents = docs.toList(),
        meta = Meta(
          isEnd = isEnd,
          pageableCount = docs.size,
          totalCount = docs.size
        )
      )
    )
}
