package kr.co.architecture.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.yeo.core.ui.BookCard
import kr.co.architecture.yeo.core.ui.NoResultContent
import kr.co.architecture.yeo.core.ui.PaginationLoadEffect
import kr.co.architecture.yeo.core.ui.SearchHeader
import kr.co.architecture.yeo.core.ui.SearchRoute
import kr.co.architecture.yeo.core.ui.SortMenuChip
import kr.co.architecture.yeo.core.ui.enums.SortUiEnum
import kr.co.architecture.yeo.core.ui.theme.BaseTheme
import kr.co.architecture.feature.search.preview.SearchUiStatePreviewParam
import kr.co.architecture.yeo.test.testing.ui.SearchTags

fun NavGraphBuilder.searchScreen() {
  composable<SearchRoute> {
    SearchScreen()
  }
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  viewModel: SearchViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is SearchUiSideEffect.Load -> {
          viewModel.fetchData(effect)
        }
      }
    }
  }

  SearchScreen(
    modifier = modifier,
    uiState = uiState,
    onQueryChange = { viewModel.setEvent(SearchUiEvent.OnQueryChange(it)) },
    onSearch = { viewModel.setEvent(SearchUiEvent.OnSearch) },
    onChangeSort = { viewModel.setEvent(SearchUiEvent.OnChangeSort(it)) },
    onClickedBookmark = { isbn: String, isBookmarked: Boolean ->
      viewModel.setEvent(SearchUiEvent.OnClickedBookmark(isbn, isBookmarked))
    },
    onClickedItem = { viewModel.setEvent(SearchUiEvent.OnClickedItem(it)) },
    onScrollToEnd = { viewModel.setEvent(SearchUiEvent.OnScrolledToEnd) }
  )
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  uiState: SearchUiState,
  onQueryChange: (String) -> Unit = {},
  onSearch: (query: String) -> Unit = {},
  onChangeSort: (SortUiEnum) -> Unit = {},
  onClickedItem: (isbn: String) -> Unit = {},
  onClickedBookmark: (isbn: String, isBookmarked: Boolean) -> Unit = { _, _ -> },
  onScrollToEnd: () -> Unit = {}
) {
  Column(modifier = modifier.fillMaxSize()) {
    SearchHeader(
      onQueryChange = onQueryChange,
      onSearch = onSearch
    ) {
      SortMenuChip(
        selected = uiState.sortUiEnum,
        options = SortUiEnum.entries.toImmutableList(),
        onChange = { onChangeSort(it as SortUiEnum) }
      )
    }

    SearchResultsSection(
      uiState = uiState,
      onClickedItem = onClickedItem,
      onClickedBookmark = onClickedBookmark,
      onScrollToEnd = onScrollToEnd
    )
  }
}

@Composable
fun SearchResultsSection(
  uiState: SearchUiState,
  onClickedItem: (String) -> Unit,
  onClickedBookmark: (String, Boolean) -> Unit,
  onScrollToEnd: () -> Unit
) {
  when (uiState.uiType) {
    SearchUiType.NONE -> Unit
    SearchUiType.EMPTY_RESULT -> NoResultContent()
    SearchUiType.LOADED_RESULT -> {
      val listState = rememberLazyListState()
      PaginationLoadEffect(
        listState = listState,
        isEnd = uiState.isEndPage,
        onScrollToEnd = onScrollToEnd
      )

      LazyColumn(
        modifier = Modifier.testTag(SearchTags.RESULT_LIST),
        state = listState
      ) {
        items(
          items = uiState.bookCardUiModels
        ) { item ->
          BookCard(
            modifier = Modifier.padding(10.dp),
            uiModel = item,
            onClickedItem = onClickedItem,
            onClickedBookmark = onClickedBookmark
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun SearchScreenPreview(
  @PreviewParameter(SearchUiStatePreviewParam::class)
  uiState: SearchUiState
) {
  BaseTheme {
    SearchScreen(
      uiState = uiState
    )
  }
}