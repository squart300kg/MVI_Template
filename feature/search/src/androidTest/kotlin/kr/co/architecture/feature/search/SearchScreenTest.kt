package kr.co.architecture.feature.search

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import junit.framework.TestCase.assertEquals
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.BookCardUiModel
import kr.co.architecture.core.ui.enums.SortUiEnum
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.test.testing.ui.SearchTags
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  private fun fakeBooks(count: Int = 5) = (1..count).map { i ->
    BookCardUiModel(
      isbn = "isbn$i",
      thumbnail = "",
      title = UiText.DynamicString("제목 $i"),
      publisher = UiText.DynamicString("출판사 $i"),
      authors = UiText.DynamicString("저자 $i"),
      isBookmarked = (i % 2 == 0),
      price = UiText.DynamicString("${i * 1000}원"),
      publishDate = UiText.DynamicString("2021-01-${"%02d".format(i)}")
    )
  }.toImmutableList()

  @Test
  fun 검색어를_입력하면_텍스트필드에_표시되고_콜백으로_전달된다() {
    var lastQuery: String? = null

    composeTestRule.setContent {
      SearchScreen(
        uiState = SearchUiState(
          uiType = SearchUiType.LOADED_RESULT,
          bookCardUiModels = fakeBooks()
        ),
        onQueryChange = { lastQuery = it }
      )
    }

    composeTestRule.onNodeWithTag(SearchTags.HEADER_TEXT_FIELD).performTextInput("미움")
    composeTestRule.onNodeWithTag(SearchTags.HEADER_TEXT_FIELD).assertTextContains("미움")

    val expected = "미움"
    val actual = lastQuery
    assertEquals(expected, actual)
  }

  @Test
  fun 키보드_검색액션을_누르면_검색어를_콜백으로_받는다() {
    var searched: String? = null

    composeTestRule.setContent {
      SearchScreen(
        uiState = SearchUiState(
          uiType = SearchUiType.LOADED_RESULT,
          bookCardUiModels = fakeBooks()
        ),
        onSearch = { searched = it }
      )
    }

    composeTestRule.onNodeWithTag(SearchTags.HEADER_TEXT_FIELD).performTextInput("심리학")
    composeTestRule.onNodeWithTag(SearchTags.HEADER_TEXT_FIELD).performImeAction()

    val expected = "심리학"
    val actual = searched
    assertEquals(expected, actual)
  }

  @Test
  fun 지우기아이콘을_누르면_검색어가_비워지고_빈문자열을_콜백으로_받는다() {
    var lastQuery: String? = null

    composeTestRule.setContent {
      SearchScreen(
        uiState = SearchUiState(
          uiType = SearchUiType.LOADED_RESULT,
          bookCardUiModels = fakeBooks()
        ),
        onQueryChange = { lastQuery = it }
      )
    }

    composeTestRule.onNodeWithTag(SearchTags.HEADER_TEXT_FIELD).performTextInput("철학")
    composeTestRule.onNodeWithTag(SearchTags.HEADER_CLEAR).assertExists().performClick()
    composeTestRule.onNodeWithTag(SearchTags.HEADER_TEXT_FIELD).assertTextContains("")

    val expected = ""
    val actual = lastQuery
    assertEquals(expected, actual)
  }

  @Test
  fun 정렬칩에서_항목을_선택하면_선택항목을_콜백으로_받는다() {
    var changed: SortUiEnum? = null

    composeTestRule.setContent {
      SearchScreen(
        uiState = SearchUiState(
          uiType = SearchUiType.LOADED_RESULT,
          sortUiEnum = SortUiEnum.ACCURACY,
          bookCardUiModels = fakeBooks()
        ),
        onChangeSort = { changed = it }
      )
    }

    composeTestRule.onNodeWithTag(SearchTags.SORT_CHIP).performClick()
    composeTestRule.onNodeWithTag(SearchTags.sortItem(SortUiEnum.LATEST.toString())).performClick()

    val expected = SortUiEnum.LATEST
    val actual = changed
    assertEquals(expected, actual)
  }

  @Test
  fun 책카드를_탭하면_해당_isbn을_콜백으로_받는다() {
    var clickedIsbn: String? = null
    val list = fakeBooks(3)
    val target = list[1]

    composeTestRule.setContent {
      SearchScreen(
        uiState = SearchUiState(
          uiType = SearchUiType.LOADED_RESULT,
          bookCardUiModels = list
        ),
        onClickedItem = { clickedIsbn = it }
      )
    }

    composeTestRule.onNodeWithTag(SearchTags.bookCard(target.isbn)).performClick()

    val expected = target.isbn
    val actual = clickedIsbn
    assertEquals(expected, actual)
  }

  @Test
  fun 북마크를_탭하면_isbn과_북마크_상태를_콜백으로_받는다() {
    var toggled: Pair<String, Boolean>? = null
    val list = fakeBooks(4)
    val target = list[2] // isbn3

    composeTestRule.setContent {
      SearchScreen(
        uiState = SearchUiState(
          uiType = SearchUiType.LOADED_RESULT,
          bookCardUiModels = list
        ),
        onClickedBookmark = { isbn, marked -> toggled = isbn to marked }
      )
    }

    composeTestRule.onNodeWithTag(SearchTags.bookmark(target.isbn)).performClick()

    val expectedIsbn = target.isbn
    val actualIsbn = toggled?.first
    assertEquals(expectedIsbn, actualIsbn)

    val expectedMarked = target.isBookmarked
    val actualMarked = toggled?.second
    assertEquals(expectedMarked, actualMarked)
  }

  @Test
  fun 리스트_끝까지_스크롤하면_페이징을_위한_콜백을_받는다() {
    var called = false
    val list = fakeBooks(50)

    composeTestRule.setContent {
      SearchScreen(
        uiState = SearchUiState(
          uiType = SearchUiType.LOADED_RESULT,
          bookCardUiModels = list,
          isEndPage = false
        ),
        onScrollToEnd = { called = true }
      )
    }

    composeTestRule
      .onNodeWithTag(SearchTags.RESULT_LIST)
      .performScrollToNode(hasTestTag(SearchTags.bookmark(list.last().isbn)))

    val expected = true
    val actual = called
    assertEquals(expected, actual)
  }
}