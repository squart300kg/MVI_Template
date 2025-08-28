package kr.co.architecture.feature.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.co.architecture.core.router.internal.navigator.Route
import kr.co.architecture.core.ui.BookItem
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.BookmarkRoute
import kr.co.architecture.core.ui.SearchHeader
import kr.co.architecture.core.ui.enums.SortTypeUiEnum
import kr.co.architecture.core.ui.util.asString

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
    viewModel.uiSideEffect.collect { effect ->
//      when (effect) {
//        is BookmarkUiSideEffect.Load -> viewModel.fetchData()
//      }
    }
  }
  BookmarkScreen(
    modifier = modifier,
    uiState = uiState,
    onQueryChange = { viewModel.setEvent(BookmarkUiEvent.OnQueryChange(it)) },
    onSearch = { viewModel.setEvent(BookmarkUiEvent.OnSearch) },
    onClickedBookmark = { isbn, isBookmarked ->
      viewModel.setEvent(BookmarkUiEvent.OnClickedBookmark(isbn, isBookmarked)) },
    onClickedItem = { viewModel.setEvent(BookmarkUiEvent.OnClickedItem(it)) },
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun BookmarkScreen(
  modifier: Modifier = Modifier,
  uiState: BookmarkUiState,
  onQueryChange: (String) -> Unit = {},
  onSearch: () -> Unit = {},
  onClickedItem: (isbn: String) -> Unit = {},
  onClickedBookmark: (isbn: String, isBookmarked: Boolean) -> Unit = { _, _ -> },
) {

  when (uiState.uiType) {
    BookmarkUiType.NONE -> {}
    BookmarkUiType.LOADED -> {
      Column(modifier = modifier.fillMaxSize()) {
        SearchHeader(
          uiModel = uiState.searchHeaderUiModel,
          onQueryChange = onQueryChange,
          onSearch = onSearch,
        )
        LazyColumn(
          modifier = modifier
            .background(Color.LightGray)
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