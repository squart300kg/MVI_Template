package kr.co.architecture.feature.search

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.ISBN
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.entity.SearchedBooks
import kr.co.architecture.core.domain.entity.SearchedBooks.Pageable
import kr.co.architecture.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.core.domain.repository.BookRepository
import kr.co.architecture.core.domain.usecase.ObserveBookmarkedBooksUseCaseImpl
import kr.co.architecture.core.domain.usecase.SearchBooksUseCaseImpl
import kr.co.architecture.core.domain.usecase.ToggleBookmarkUseCaseImpl
import kr.co.architecture.core.ui.GlobalUiBus
import kr.co.architecture.core.ui.util.formatter.DateTextFormatter
import kr.co.architecture.core.ui.util.formatter.MoneyTextFormatter
import kr.co.architecture.feature.search.SearchUiSideEffect.Load
import kr.co.architecture.navigation.Navigator
import kr.co.architecture.navigation.Route
import kr.co.architecture.navigation.routes.DetailRoute
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@file:OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

  // fakes
  private lateinit var fakeSearchBooks: FakeSearchBooksUseCase
  private lateinit var fakeObserveBookmarked: FakeObserveBookmarkedBooksUseCase
  private lateinit var fakeToggleBookmark: FakeToggleBookmarkUseCase
  private lateinit var dateFormatter: DateTextFormatter
  private lateinit var moneyFormatter: MoneyTextFormatter

  // test doubles
  private lateinit var testNavigator: TestNavigator
  private lateinit var testUiBus: TestGlobalUiBus

  private lateinit var viewModel: SearchViewModel

  @Before
  fun setup() {
    fakeSearchBooks = FakeSearchBooksUseCase()
    fakeObserveBookmarked = FakeObserveBookmarkedBooksUseCase()
    fakeToggleBookmark = FakeToggleBookmarkUseCase()

    dateFormatter = object : DateTextFormatter {
      // 테스트 편의상 간단 반환
      override fun invoke(raw: String): String = raw
    }
    moneyFormatter = object : MoneyTextFormatter {
      override fun invoke(amount: Int): String = amount.toString()
    }

    viewModel = SearchViewModel(
      searchBooksUseCase = fakeSearchBooks,
      observeBookmarkedBooksUseCase = fakeObserveBookmarked,
      toggleBookmarkUseCase = fakeToggleBookmark,
      dateTextFormatter = dateFormatter,
      moneyTextFormatter = moneyFormatter,
    )

    testNavigator = TestNavigator()
    testUiBus = TestGlobalUiBus()
    viewModel.injectNavigator(testNavigator)
    viewModel.injectGlobalUiBus(testUiBus)
  }

  // when: 검색 버튼을 누르면  then: 리스트 초기화 & Load.First 사이드이펙트 방출
  @Test
  fun 검색버튼누르면_리스트초기화되고_LoadFirst_방출() = runTest {
    // 미리 리스트 채워진 상태 흉내
    viewModel.setEvent(SearchUiEvent.OnChangeSort(SortUiEnum.ACCURACY))
    advanceUntilIdle()

    val effectDeferred = async { viewModel.uiSideEffect.first() }
    viewModel.setEvent(SearchUiEvent.OnSearch)
    val effect = effectDeferred.await()

    val expected = Load.First
    val actual = effect
    assertEquals(expected, actual)

    val expectedSize = 0
    val actualSize = viewModel.uiState.value.bookCardUiModels.size
    assertEquals(expectedSize, actualSize)
  }

  // when: 끝까지 스크롤 이벤트  then: Load.More 방출
  @Test
  fun 끝까지스크롤하면_LoadMore_방출() = runTest {
    val effectDeferred = async { viewModel.uiSideEffect.first() }
    viewModel.setEvent(SearchUiEvent.OnScrolledToEnd)
    val effect = effectDeferred.await()

    val expected = Load.More
    val actual = effect
    assertEquals(expected, actual)
  }

  // when: fetchData(First) 성공  then: 상태 갱신 & query/페이지 전달 확인 & 로딩 on/off 호출
  @Test
  fun FetchData_First_성공시_상태업데이트와_로딩호출() = runTest {
    // given
    fakeSearchBooks.result = searchedBooksOf(
      listOf(
        book(isbn = "1"),
        book(isbn = "2")
      ),
      isEnd = false
    )

    // query 변경
    viewModel.setEvent(SearchUiEvent.OnQueryChange("심리학"))
    advanceUntilIdle()

    // when
    viewModel.fetchData(Load.First)
    advanceUntilIdle()

    // then
    val state = viewModel.uiState.value
    val expectedType = SearchUiType.LOADED_RESULT
    val actualType = state.uiType
    assertEquals(expectedType, actualType)

    val expectedCount = 2
    val actualCount = state.bookCardUiModels.size
    assertEquals(expectedCount, actualCount)

    val expectedQuery = "심리학"
    val actualQuery = fakeSearchBooks.lastParams?.query
    assertEquals(expectedQuery, actualQuery)

    val expectedPage = 1
    val actualPage = fakeSearchBooks.lastParams?.page
    assertEquals(expectedPage, actualPage)

    // 로딩 on/off 확인
    assertTrue(testUiBus.loadingOnCount > 0)
    assertTrue(testUiBus.loadingOffCount > 0)
  }

  // when: fetchData(More) 이어 호출  then: 리스트가 이어붙여지고 페이지 증가
  @Test
  fun FetchData_More_성공시_리스트이어붙임과_페이지증가() = runTest {
    // First
    fakeSearchBooks.result = searchedBooksOf(
      listOf(book("1"), book("2")),
      isEnd = false
    )
    viewModel.fetchData(Load.First)
    advanceUntilIdle()

    // More
    fakeSearchBooks.result = searchedBooksOf(
      listOf(book("3"), book("4")),
      isEnd = true
    )
    viewModel.fetchData(Load.More)
    advanceUntilIdle()

    val state = viewModel.uiState.value
    val expectedSize = 4
    val actualSize = state.bookCardUiModels.size
    assertEquals(expectedSize, actualSize)

    val expectedIsEnd = true
    val actualIsEnd = state.isEndPage
    assertEquals(expectedIsEnd, actualIsEnd)
  }

  // when: 아이템 클릭  then: 상세로 네비게이션
  @Test
  fun 아이템클릭시_상세화면으로_이동() = runTest {
    viewModel.setEvent(SearchUiEvent.OnClickedItem("abc-isbn"))
    advanceUntilIdle()

    val expectedRoute = DetailRoute("abc-isbn")
    val actualRoute = testNavigator.lastNavigatedRoute
    assertEquals(expectedRoute, actualRoute)
  }

  // when: 북마크 토글 클릭  then: ToggleBookmarkUseCase 호출
  @Test
  fun 북마크토글시_usecase호출() = runTest {
    viewModel.setEvent(SearchUiEvent.OnClickedBookmark("x-isbn", isBookmarked = false))
    advanceUntilIdle()

    val expectedIsbn = ISBN("x-isbn")
    val actualIsbn = fakeToggleBookmark.lastParams?.isbn
    assertEquals(expectedIsbn, actualIsbn)

    val expectedType = BookmarkToggleTypeEnum.SAVE
    val actualType = fakeToggleBookmark.lastParams?.bookmarkToggleTypeEnum
    assertEquals(expectedType, actualType)
  }

  // when: 즐겨찾기 목록이 변경되면  then: 현재 UI 목록의 isBookmarked가 반영된다
  @Test
  fun 즐겨찾기변경시_UI_표시상태_업데이트() = runTest {
    // UI 목록 채우기
    fakeSearchBooks.result = searchedBooksOf(listOf(book("A"), book("B")), isEnd = true)
    viewModel.fetchData(Load.First)
    advanceUntilIdle()

    // 즐겨찾기 flow에 "B" 발행
    fakeObserveBookmarked.emit(
      listOf(book("B", isBookmarked = true))
    )
    advanceUntilIdle()

    val state = viewModel.uiState.value
    val expected = true
    val actual = state.bookCardUiModels.first { it.isbn == "B" }.isBookmarked
    assertEquals(expected, actual)
  }

  // when: 정렬 변경  then: 리스트 초기화 & Load.First
  @Test
  fun 정렬변경시_리스트초기화_LoadFirst() = runTest {
    // 미리 채워진 상태처럼 보이게
    fakeSearchBooks.result = searchedBooksOf(listOf(book("1")), isEnd = true)
    viewModel.fetchData(Load.First)
    advanceUntilIdle()

    val effectDeferred = async { viewModel.uiSideEffect.first() }
    viewModel.setEvent(SearchUiEvent.OnChangeSort(SortUiEnum.LATEST))
    val effect = effectDeferred.await()

    val expectedEffect = Load.First
    val actualEffect = effect
    assertEquals(expectedEffect, actualEffect)

    val expectedCount = 0
    val actualCount = viewModel.uiState.value.bookCardUiModels.size
    assertEquals(expectedCount, actualCount)
  }

  // --------------------------------------------------------------------------
  // helpers / fakes
  // --------------------------------------------------------------------------

  private fun book(
    isbn: String,
    title: String = "t-$isbn",
    authors: List<String> = listOf("auth"),
    publisher: String = "pub",
    dateTime: String = "2021-01-01T00:00:00.000+09:00",
    price: Price = Price.Origin(origin = 1000),
    url: String = "",
    thumbnail: String = "",
    isBookmarked: Boolean = false
  ) = Book(
    isbn = isbn,
    title = title,
    authors = authors,
    publisher = publisher,
    dateTime = dateTime, // String을 쓰는 도메인이라 가정(프로덕션 정의에 맞추세요)
    price = price,
    url = url,
    thumbnail = thumbnail,
    contents = "",
    isBookmarked = isBookmarked
  )

  private fun searchedBooksOf(list: List<Book>, isEnd: Boolean) =
    SearchedBooks(
      books = list,
      pageable = Pageable(isEnd = isEnd)
    )

  // --- Fakes ---

  private class FakeSearchBooksUseCase : SearchBooksUseCaseImpl(
    bookRepository = object : BookRepository {
      override fun observeBookmarkedBooks() = flowOf(emptyList<Book>())
      override suspend fun searchBooks(params: SearchBooksUseCaseImpl.Params): SearchedBooks {
        throw IllegalStateException("not used directly")
      }
      override suspend fun toggleBookmark(params: ToggleBookmarkUseCaseImpl.Params) {}
      override suspend fun searchBook(params: kr.co.architecture.core.domain.usecase.SearchBookUseCaseImpl.Params): Book? = null
    }
  ) {
    var lastParams: Params? = null
    var result: SearchedBooks = SearchedBooks(emptyList(), Pageable(isEnd = true))

    override suspend operator fun invoke(params: Params): SearchedBooks {
      lastParams = params
      return result
    }
  }

  private class FakeObserveBookmarkedBooksUseCase : ObserveBookmarkedBooksUseCaseImpl(
    bookRepository = object : BookRepository {
      override fun observeBookmarkedBooks() = flowOf(emptyList<Book>())
      override suspend fun searchBooks(params: SearchBooksUseCaseImpl.Params): SearchedBooks {
        throw IllegalStateException("not used")
      }
      override suspend fun toggleBookmark(params: ToggleBookmarkUseCaseImpl.Params) {}
      override suspend fun searchBook(params: kr.co.architecture.core.domain.usecase.SearchBookUseCaseImpl.Params): Book? = null
    }
  ) {
    private val upstream = MutableSharedFlow<List<Book>>(replay = 0, extraBufferCapacity = 16)
    override operator fun invoke() = upstream
    suspend fun emit(list: List<Book>) = upstream.emit(list)
  }

  private class FakeToggleBookmarkUseCase : ToggleBookmarkUseCaseImpl(
    bookRepository = object : BookRepository {
      override fun observeBookmarkedBooks() = flowOf(emptyList<Book>())
      override suspend fun searchBooks(params: SearchBooksUseCaseImpl.Params): SearchedBooks {
        throw IllegalStateException("not used")
      }
      override suspend fun toggleBookmark(params: ToggleBookmarkUseCaseImpl.Params) {}
      override suspend fun searchBook(params: kr.co.architecture.core.domain.usecase.SearchBookUseCaseImpl.Params): Book? = null
    },
    dispatcher = StandardTestDispatcher()
  ) {
    var lastParams: Params? = null
    override suspend operator fun invoke(params: Params) {
      lastParams = params
    }
  }

  private class TestNavigator : Navigator {
    var lastNavigatedRoute: Route? = null
    override suspend fun navigateBack() {}
    override suspend fun navigateWeb(url: String) {}
    override suspend fun navigate(route: Route, saveState: Boolean, launchSingleTop: Boolean) {
      lastNavigatedRoute = route
    }
  }

  private class TestGlobalUiBus : GlobalUiBus() {
    var loadingOnCount = 0
    var loadingOffCount = 0
    var lastFailure: Throwable? = null

    override fun setLoadingState(loadingState: Boolean) {
      if (loadingState) loadingOnCount++ else loadingOffCount++
      // super 호출로 실제 StateFlow를 만들 필요는 테스트에선 없음
    }
    override fun showFailureDialog(throwable: Throwable) {
      lastFailure = throwable
    }
  }
}

/** JUnit Rule: 메인 디스패처 교체 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
  private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
  override fun starting(description: org.junit.runner.Description) {
    Dispatchers.setMain(dispatcher)
  }
  override fun finished(description: org.junit.runner.Description) {
    Dispatchers.resetMain()
  }
}
