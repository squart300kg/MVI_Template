package kr.co.architecture.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.ui.BookItem
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.SearchHeader
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.enums.SortTypeUiEnum
import kr.co.architecture.core.ui.R as coreUiR

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
  onChangeSort: (SortTypeUiEnum) -> Unit = {},
  onClickedItem: (isbn: String) -> Unit = {},
  onClickedBookmark: (isbn: String, isBookmarked: Boolean) -> Unit = { _, _ -> },
  onScrollToEnd: () -> Unit = {}
) {
  Column(modifier = modifier.fillMaxSize()) {
    SearchHeader(
      uiModel = uiState.searchHeaderUiModel,
      onQueryChange = onQueryChange,
      onSearch = onSearch,
      onChangeSort = onChangeSort
    )

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
          modifier = modifier
            .background(Color.LightGray),
          state = listState
        ) {
          items(
            items = uiState.bookUiModels
          ) { item ->
            BookItem(
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