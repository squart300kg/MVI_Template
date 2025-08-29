package kr.co.architecture.feature.search

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.BookCard
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.SearchHeader
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.SortMenuChip
import kr.co.architecture.core.ui.enums.SortUiEnum

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
    onClickedBookmark = { isbn, isBookmarked ->
      viewModel.setEvent(SearchUiEvent.OnClickedBookmark(isbn, isBookmarked)) },
    onClickedItem = { viewModel.setEvent(SearchUiEvent.OnClickedItem(it)) },
    onScrollToEnd = { viewModel.setEvent(SearchUiEvent.OnScrolledToEnd) }
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
      uiModel = uiState.searchHeaderUiModel,
      onQueryChange = onQueryChange,
      onSearch = onSearch,
    ) {
      SortMenuChip(
        selected = uiState.sort,
        options = SortUiEnum.entries.toImmutableList(),
        onChange = { onChangeSort(it as SortUiEnum) }
      )
    }

    when (uiState.uiType) {
      SearchUiType.NONE -> {}
      SearchUiType.LOADED -> {
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
            BookCard(
              modifier = Modifier
                .padding(10.dp),
              uiModel = item,
              onClickedBookmark = { onClickedBookmark(item.isbn, item.isBookmarked) },
              onClickedItem = { onClickedItem(item.isbn) }
            )
          }
        }
      }
    }
  }
}

//@Preview
//@Composable
//fun BookItemPreview() {
//  BookItem(
//    modifier = Modifier.background(Color.White),
//    uiModel = UiModel()
//  )
//}