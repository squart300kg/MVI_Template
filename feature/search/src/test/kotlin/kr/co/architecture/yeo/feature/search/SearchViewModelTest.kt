package kr.co.architecture.yeo.feature.search

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kr.co.architecture.yeo.core.domain.entity.Book
import kr.co.architecture.yeo.core.domain.entity.Pageable
import kr.co.architecture.yeo.core.domain.entity.Price
import kr.co.architecture.yeo.core.domain.entity.SearchedBooks
import kr.co.architecture.yeo.core.domain.enums.BookmarkToggleTypeEnum
import kr.co.architecture.yeo.core.ui.DetailRoute
import kr.co.architecture.yeo.core.ui.enums.SortUiEnum
import kr.co.architecture.yeo.core.ui.util.formatter.KoreanDateTextFormatter
import kr.co.architecture.yeo.core.ui.util.formatter.KoreanMoneyTextFormatter
import kr.co.architecture.feature.search.SearchUiEvent
import kr.co.architecture.feature.search.SearchUiSideEffect
import kr.co.architecture.feature.search.SearchUiType
import kr.co.architecture.feature.search.SearchViewModel
import kr.co.architecture.yeo.feature.search.fake.FakeGlobalUiBus
import kr.co.architecture.yeo.feature.search.fake.FakeNavigator
import kr.co.architecture.yeo.feature.search.fake.FakeObserveBookmarkedBooksUseCase
import kr.co.architecture.yeo.feature.search.fake.FakeSearchBooksUseCase
import kr.co.architecture.yeo.feature.search.fake.FakeToggleBookmarkUseCase
import kr.co.architecture.yeo.test.testing.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO: 개선점
//  1. FakeUseCase내, public var에 private set 적용
//  하지만 @Test 메서드는 매 실행 전, @Before을 통한 신규 객체 생성이 이뤄진다.
//  즉, 객체 내부 상태가 비워지므로, 하나의 @Test 내부의 상태 트래킹에 문제가없다.
//  2. FakeObserveBookmarkedBooksUseCase내, 버퍼 갯수 안잡아도 됐음

class SearchViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

  private lateinit var viewModel: SearchViewModel
  private lateinit var searchBooksUseCase: FakeSearchBooksUseCase
  private lateinit var observeBookmarksUseCase: FakeObserveBookmarkedBooksUseCase
  private lateinit var toggleBookmarkUseCase: FakeToggleBookmarkUseCase
  private lateinit var globalBus: FakeGlobalUiBus
  private lateinit var navigator: FakeNavigator

  @Before
  fun setup() {
    searchBooksUseCase = FakeSearchBooksUseCase()
    observeBookmarksUseCase = FakeObserveBookmarkedBooksUseCase()
    toggleBookmarkUseCase = FakeToggleBookmarkUseCase()
    globalBus = FakeGlobalUiBus()
    navigator = FakeNavigator()

    viewModel = SearchViewModel(
      searchBooksUseCase = searchBooksUseCase,
      observeBookmarkedBooksUseCase = observeBookmarksUseCase,
      toggleBookmarkUseCase = toggleBookmarkUseCase,
      dateTextFormatter = KoreanDateTextFormatter(),
      moneyTextFormatter = KoreanMoneyTextFormatter()
    ).also {
      it.injectNavigator(navigator)
      it.injectGlobalUiBus(globalBus)
    }
  }

  @Test
  fun 검색_요청하면_리스트가_로딩되고_UI가_업데이트된다() = runTest {
    // GIVEN
    searchBooksUseCase.result = searchedBooks(count = 3, end = false)

    val job = launch {
      viewModel.uiSideEffect.collect { sideEffect ->
        if (sideEffect is SearchUiSideEffect.Load) {
          viewModel.fetchData(sideEffect)
        }
      }
    }

    // WHEN
    viewModel.setEvent(SearchUiEvent.OnQueryChange("미움"))
    viewModel.setEvent(SearchUiEvent.OnSearch)

    advanceUntilIdle()

    // THEN
    val state = viewModel.uiState.value
    assertEquals(SearchUiType.LOADED_RESULT, state.uiType)
    assertEquals(3, state.bookCardUiModels.size)
    assertEquals(false, state.isEndPage)

    // TODO: 왜 이렇게?
    assertTrue(globalBus.loadingHistory.first() == true)
    assertTrue(globalBus.loadingHistory.last() == false)

    assertEquals("미움", searchBooksUseCase.lastParams?.query)

    job.cancel()
  }

  @Test
  fun 스크롤끝_요청하면_페이징을_위한_사이드이펙트가_발행된다() = runTest {
    // GIVEN
    val effects = mutableListOf<SearchUiSideEffect>()
    val job = launch {
      viewModel.uiSideEffect.collect { effects += it }
    }

    // WHEN
    viewModel.setEvent(SearchUiEvent.OnScrolledToEnd)
    advanceUntilIdle()

    // THEN
    assertTrue(effects.any { it is SearchUiSideEffect.Load.More })
    job.cancel()
  }

  @Test
  fun 아이템_탭하면_상세로_네비게이션된다() = runTest {
    // WHEN
    viewModel.setEvent(SearchUiEvent.OnClickedItem(isbn = "isbn1"))
    advanceUntilIdle()

    // then
    val route = navigator.latestNavigatingRoute as? DetailRoute
    assertEquals("isbn1", route?.isbn)
  }

  @Test
  fun 북마크_클릭하면_ToggleBookmarkUseCase가_호출된다() = runTest {
    // WHEN
    viewModel.setEvent(SearchUiEvent.OnClickedBookmark(isbn = "isbn2", isBookmarked = false))
    advanceUntilIdle()

    // THEN
    val params = toggleBookmarkUseCase.latestClickedParams
    assertEquals("isbn2", params?.isbn?.value)
    assertEquals(BookmarkToggleTypeEnum.SAVE, params?.bookmarkToggleTypeEnum)
  }

  @Test
  fun 북마크_관찰_데이터가_도착하면_리스트의_표시상태가_동기화된다() = runTest {
    // GIVEN
    searchBooksUseCase.result = searchedBooks(count = 2, end = true)
    val job = launch {
      viewModel.uiSideEffect.collect { sideEffect ->
        if (sideEffect is SearchUiSideEffect.Load) viewModel.fetchData(sideEffect)
      }
    }
    viewModel.setEvent(SearchUiEvent.OnSearch)
    advanceUntilIdle()

    // WHEN: 북마크 Flow에 isbn1 발행
    observeBookmarksUseCase.emit(listOf(makeBook(1, bookmarked = true)))
    advanceUntilIdle()

    // THEN: uiState 에서 isbn1 항목 isBookmarked=true 로 동기화 되었는지
    val item1 = viewModel.uiState.value.bookCardUiModels.first { it.isbn == "isbn1" }
    assertTrue(item1.isBookmarked)

    job.cancel()
  }

  @Test
  fun 정렬칩을_변경하면_검색결과를_지우고_처음부터_로딩한다() = runTest {
    // GIVEN
    val effectDeferred = async { viewModel.uiSideEffect.first() }

    // WHEN
    viewModel.setEvent(SearchUiEvent.OnChangeSort(SortUiEnum.LATEST))
    advanceUntilIdle()

    // THEN - 상태
    val expectedSort = SortUiEnum.LATEST
    val actualSort = viewModel.uiState.value.sortUiEnum
    assertEquals(expectedSort, actualSort)

    val expectedEmpty = true
    val actualEmpty = viewModel.uiState.value.bookCardUiModels.isEmpty()
    assertEquals(expectedEmpty, actualEmpty)

    val effect = effectDeferred.await()
    val isLoadFirst = effect is SearchUiSideEffect.Load.First
    assertTrue(isLoadFirst)
  }

  fun makeBook(
    i: Int,
    bookmarked: Boolean = false
  ): Book = Book(
    isbn = "isbn$i",
    title = "제목 $i",
    authors = listOf("저자 $i"),
    publisher = "출판사 $i",
    dateTime = "2021-01-${"%02d".format(i)}T00:00:00.000+09:00",
    price = Price.Origin(origin = i * 1000),
    url = "https://example.com/$i",
    thumbnail = "",
    contents = "설명 $i",
    isBookmarked = bookmarked
  )

  fun searchedBooks(count: Int, end: Boolean = false) = SearchedBooks(
    books = (1..count).map { makeBook(it) },
    pageable = Pageable(isEnd = end)
  )
}