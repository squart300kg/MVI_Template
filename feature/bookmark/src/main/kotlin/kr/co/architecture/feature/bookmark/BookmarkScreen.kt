package kr.co.architecture.feature.bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.ui.BookCard
import kr.co.architecture.core.ui.BookmarkRoute
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.NoResultContent
import kr.co.architecture.core.ui.SearchHeader
import kr.co.architecture.core.ui.SortMenuChip
import kr.co.architecture.core.ui.enums.SortPriceRangeUiEnum
import kr.co.architecture.core.ui.enums.SortDirectionUiEnum

fun NavGraphBuilder.bookmarkScreen() {
  composable<BookmarkRoute> {
    BookmarkScreen()
  }
}

@Composable
fun BookmarkScreen(
  modifier: Modifier = Modifier,
  viewModel: BookmarkViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect -> }
  }
  BookmarkScreen(
    modifier = modifier,
    uiState = uiState,
    onQueryChange = { viewModel.setEvent(BookmarkUiEvent.OnQueryChange(it)) },
    onClickedBookmark = { isbn, isBookmarked ->
      viewModel.setEvent(BookmarkUiEvent.OnClickedBookmark(isbn, isBookmarked))
    },
    onClickedItem = { viewModel.setEvent(BookmarkUiEvent.OnClickedItem(it)) },
    onChangeDirectionSort = { viewModel.setEvent(BookmarkUiEvent.OnChangeSortDirection(it)) },
    onChangePriceSort = { viewModel.setEvent(BookmarkUiEvent.OnChangePriceRange(it)) },
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun BookmarkScreen(
  modifier: Modifier = Modifier,
  uiState: BookmarkUiState,
  onQueryChange: (String) -> Unit = {},
  onClickedItem: (isbn: String) -> Unit = {},
  onClickedBookmark: (isbn: String, isBookmarked: Boolean) -> Unit = { _, _ -> },
  onChangeDirectionSort: (SortDirectionUiEnum) -> Unit = {},
  onChangePriceSort: (SortPriceRangeUiEnum) -> Unit = {},
) {
  Column(modifier = modifier.fillMaxSize()) {
    SearchHeader(
      query = uiState.query,
      onQueryChange = onQueryChange
    ) {
      SortMenuChip(
        selected = uiState.sortPriceRangeUiEnum,
        options = SortPriceRangeUiEnum.entries.toImmutableList(),
        onChange = {
          onChangePriceSort(it as SortPriceRangeUiEnum)
        }
      )
      SortMenuChip(
        selected = uiState.sortDirectionUiEnum,
        options = SortDirectionUiEnum.entries.toImmutableList(),
        onChange = {
          onChangeDirectionSort(it as SortDirectionUiEnum)
        }
      )
    }
    when (uiState.uiType) {
      BookmarkUiType.NONE -> {}
      BookmarkUiType.EMPTY_RESULT -> NoResultContent()
      BookmarkUiType.LOADED_RESULT -> {
        LazyColumn {
          items(
            items = uiState.bookUiModels
          ) { item ->
            BookCard(
              modifier = Modifier
                .padding(10.dp),
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