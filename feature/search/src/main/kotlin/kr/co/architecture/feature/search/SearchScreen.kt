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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.BookCard
import kr.co.architecture.core.ui.BookUiModel
import kr.co.architecture.core.ui.NoResultContent
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.SearchHeader
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.SortMenuChip
import kr.co.architecture.core.ui.enums.SortUiEnum
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.feature.search.preview.SearchUiStatePreviewParam

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
        selected = uiState.sort,
        options = SortUiEnum.entries.toImmutableList(),
        onChange = { onChangeSort(it as SortUiEnum) }
      )
    }

    SearchResultsSection(
      uiType = uiState.uiType,
      uiModels = uiState.bookUiModels,
      isPageable = uiState.isPageable,
      onClickedItem = onClickedItem,
      onClickedBookmark = onClickedBookmark,
      onScrollToEnd = onScrollToEnd
    )
  }
}

@Composable
fun SearchResultsSection(
  uiType: SearchUiType,
  uiModels: ImmutableList<BookUiModel>,
  isPageable: Boolean,
  onClickedItem: (String) -> Unit,
  onClickedBookmark: (String, Boolean) -> Unit,
  onScrollToEnd: () -> Unit
) {
  when (uiType) {
    SearchUiType.NONE -> Unit
    SearchUiType.EMPTY_RESULT -> NoResultContent()
    SearchUiType.LOADED_RESULT -> {
      val listState = rememberLazyListState()
      PaginationLoadEffect(listState, isPageable, onScrollToEnd)

      LazyColumn(state = listState) {
        items(
          items = uiModels
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