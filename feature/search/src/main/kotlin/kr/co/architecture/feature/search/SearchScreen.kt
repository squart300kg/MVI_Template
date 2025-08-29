package kr.co.architecture.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.BookCard
import kr.co.architecture.core.ui.GlobalUiStateEffect
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
    onQueryChange = remember(viewModel) { { query: String ->
      viewModel.setEvent(SearchUiEvent.OnQueryChange(query))
    } },
    onSearch = remember(viewModel) { {
      viewModel.setEvent(SearchUiEvent.OnSearch)
    } },
    onChangeSort = remember(viewModel) { { uiEnum: SortUiEnum ->
      viewModel.setEvent(SearchUiEvent.OnChangeSort(uiEnum))
    } },
    onClickedBookmark = remember(viewModel) { { isbn: String, isBookmarked: Boolean ->
      viewModel.setEvent(SearchUiEvent.OnClickedBookmark(isbn, isBookmarked))
    } },
    onClickedItem = remember(viewModel) { { isbn: String ->
      viewModel.setEvent(SearchUiEvent.OnClickedItem(isbn))
    } },
    onScrollToEnd = remember(viewModel) { {
      viewModel.setEvent(SearchUiEvent.OnScrolledToEnd)
    } }
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  uiState: SearchUiState,
  onQueryChange: (String) -> Unit = {},
  onSearch: () -> Unit = {},
  onChangeSort: (SortUiEnum) -> Unit = {},
  onClickedItem: (isbn: String) -> Unit = {},
  onClickedBookmark: (isbn: String, isBookmarked: Boolean) -> Unit = { _, _ -> },
  onScrollToEnd: () -> Unit = {}
) {
  Column(modifier = modifier.fillMaxSize()) {
    SearchHeader(
      query = uiState.query,
      onQueryChange = onQueryChange,
      onSearch = onSearch,
    ) {
      SortMenuChip(
        selected = uiState.sort,
        options = SortUiEnum.entries.toImmutableList(),
        onChange = { onChangeSort(it as SortUiEnum) }
      )
    }

    // TODO: 타이핑에따른 리컴포지션 개선하기
    when (uiState.uiType) {
      SearchUiType.NONE -> {}
      SearchUiType.EMPTY_RESULT -> NoResultContent()
      SearchUiType.LOADED_RESULT -> {
        val listState = rememberLazyListState()
        PaginationLoadEffect(
          listState = listState,
          isEnd = uiState.isPageable,
          onScrollToEnd = onScrollToEnd
        )
        LazyColumn(
          state = listState
        ) {
          items(
            items = uiState.bookUiModels
          ) { item ->
            SideEffect { println("lazyColumnHssh BookCard outter, item: ${item}") }

            BookCard(
              modifier = Modifier.padding(10.dp),
              uiModel = item,
              onClickedBookmark = onClickedBookmark,
              onClickedItem = onClickedItem
            )
          }
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