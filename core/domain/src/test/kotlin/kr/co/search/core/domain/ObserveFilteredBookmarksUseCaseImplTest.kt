package kr.co.search.core.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kr.co.architecture.core.domain.entity.Book
import kr.co.architecture.core.domain.entity.Price
import kr.co.architecture.core.domain.enums.SortDirectionEnum
import kr.co.architecture.core.domain.enums.SortPriceRangeEnum
import kr.co.architecture.core.domain.usecase.ObserveFilteredBookmarksUseCase.BookmarkFilter
import kr.co.architecture.core.domain.usecase.ObserveFilteredBookmarksUseCaseImpl
import kr.co.search.core.domain.fake.FakeBookRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * 즐겨찾기 목록 필터링/정렬 유즈케이스 테스트.
 *
 * - 검색어 매칭(제목/출판사/저자)
 * - 가격 필터(미만/이상 + 임계값)
 * - 제목 기준 정렬(오름/내림)
 * - 위 조건들의 조합
 */
class ObserveFilteredBookmarksUseCaseImplTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

  private lateinit var repository: FakeBookRepository
  private lateinit var useCase: ObserveFilteredBookmarksUseCaseImpl

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

  /**
   * 샘플 데이터
   * - 검색어로써, "심리", "A", "B" 등에 반응하도록 제목·출판사·저자 값을 구성
   * - 가격은 10,000구간 기준으로 나눔
   */
  private val sample = listOf(
    makeBook(1).copy(publisher = "A 줄판사",title = "심리학의 즐거움", authors = listOf("홍길동"), price = Price.Origin(9_000)),
    makeBook(2).copy(publisher = "A 줄판사",title = "미움받을 용기", authors = listOf("기시미 이치로"), price = Price.Origin(12_000)),
    makeBook(3).copy(publisher = "B 줄판사",title = "동물농장", authors = listOf("George Orwell"), price = Price.Origin(25_000)),
    makeBook(4).copy(publisher = "B 줄판사",title = "Clean Architecture", authors = listOf("Robert C. Martin"), price = Price.Origin(30_000)),
    makeBook(5).copy(publisher = "C 줄판사",title = "심리 실험", authors = listOf("김철수A"), price = Price.Origin(5_000)),
    makeBook(6).copy(publisher = "C 줄판사",title = "채식주의자", authors = listOf("한강"), price = Price.Origin(10000)),
  )

  @Before
  fun setup() {
    repository = FakeBookRepository()
    repository.books.value = sample
    useCase = ObserveFilteredBookmarksUseCaseImpl(repository)
  }

  @Test
  fun 제목이_포함된_검색어를_필터링한다() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(query = "심리")
    )

    // when
    val result = useCase(filters).first()

    // then
    assertEquals(listOf(sample[4].isbn, sample[0].isbn), result.map { it.isbn })
  }

  @Test
  fun 출판사가_포함된_검색어를_필터링한다() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(query = "B")
    )

    // when
    val result = useCase(filters).first()

    // then
    assertEquals(listOf(sample[3].isbn, sample[2].isbn), result.map { it.isbn })
  }

  @Test
  fun 저자가_포함된_검색어를_필터링한다() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(query = "홍길동")
    )

    // when
    val result = useCase(filters).first()

    // then
    assertEquals(listOf(sample[0].isbn), result.map { it.isbn })
  }

  @Test
  fun 제목_출판사_저자가_포함된_검색어를_필터링한다() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(query = "A")
    )

    // when
    val result = useCase(filters).first()

    // then
    assertEquals(listOf(sample[3].isbn, sample[1].isbn, sample[4].isbn, sample[0].isbn), result.map { it.isbn })
  }

  @Test
  fun 가격필터_10000원_미만_적용하면_10000원_미만_책만_필터링된다() = runTest {
    // given:
    val filters = MutableStateFlow(
      BookmarkFilter(
        query = "",
        sortDirection = SortDirectionEnum.ASCENDING,
        priceRange = SortPriceRangeEnum.LESS,
        threshold = 10_000
      )
    )

    // when
    val result = useCase(filters).first()

    // then:
    val expectedIsbns = listOf(sample[4].isbn, sample[0].isbn)
    assertEquals(expectedIsbns, result.map { it.isbn })
  }

  @Test
  fun 가격필터_10000원_이상_적용하면_10000원_이상_책만_필터링된다() = runTest {
    // given: 10,000 이상
    val filters = MutableStateFlow(
      BookmarkFilter(
        query = "",
        sortDirection = SortDirectionEnum.ASCENDING,
        priceRange = SortPriceRangeEnum.MORE,
        threshold = 10_000
      )
    )

    // when
    val result = useCase(filters).first()

    // then
    val expectedIsbnsAscByTitle = listOf(sample[3].isbn, sample[2].isbn, sample[1].isbn, sample[5].isbn)
    assertEquals(expectedIsbnsAscByTitle, result.map { it.isbn })
  }

  @Test
  fun 정렬방향_내림차순이_적용되면_제목기준_내림차순으로_받는다() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(
        query = "",
        sortDirection = SortDirectionEnum.DESCENDING,
        priceRange = SortPriceRangeEnum.ALL
      )
    )

    // when
    val result = useCase(filters).first()

    // then: 제목 내림차순
    assertEquals(
      sample.sortedByDescending { it.title }.map { it.isbn },
      result.map { it.isbn }
    )
  }

  @Test
  fun 정렬방향_오름차순이_적용되면_제목기준_오름차순으로_받는다() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(
        query = "",
        sortDirection = SortDirectionEnum.ASCENDING,
        priceRange = SortPriceRangeEnum.ALL
      )
    )

    // when
    val result = useCase(filters).first()

    // then: 제목 내림차순
    assertEquals(
      sample.sortedBy { it.title }.map { it.isbn },
      result.map { it.isbn }
    )
  }

  @Test
  fun 검색어_A_10000원_이상_오름차순_동시필터링() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(
        query = "A",
        sortDirection = SortDirectionEnum.ASCENDING,
        priceRange = SortPriceRangeEnum.MORE
      )
    )

    // when
    val result = useCase(filters).first()

    // then
    val expected = listOf(sample[3].isbn, sample[1].isbn)
    val actual = result.map { it.isbn }
    assertEquals(expected, actual)
  }

  @Test
  fun 검색어_A_금액_미만_내림차순_동시필터링() = runTest {
    // given
    val filters = MutableStateFlow(
      BookmarkFilter(
        query = "A",
        sortDirection = SortDirectionEnum.DESCENDING,
        priceRange = SortPriceRangeEnum.LESS,
        threshold = 10_000
      )
    )

    // when
    val result = useCase(filters).first()

    // then
    val expected = listOf(sample[0].isbn, sample[4].isbn)
    val actual = result.map { it.isbn }
    assertEquals(expected, actual)
  }
}
