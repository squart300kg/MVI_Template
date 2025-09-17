package kr.co.architecture.feature.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.domain.GetSortedImagesAndVideosByRecentlyUseCase
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.util.asString

fun NavGraphBuilder.searchScreen() {
  composable<SearchRoute> {
    SearchScreen()
  }
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: SearchViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiSideEffect.collect { effect ->
        when (effect) {
          is SearchUiSideEffect.Load -> viewModel.fetchData(effect)
        }
      }
    }
  }

  SearchScreen(
    modifier = modifier,
    uiState = uiState,
    onClickedItem = {},
    onScrollToEnd = { viewModel.setEvent(SearchUiEvent.OnScrolledToEnd) }
  )
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  uiState: SearchUiState,
  onClickedItem: (UiModel) -> Unit = {},
  onScrollToEnd: () -> Unit
) {

  when (uiState.uiType) {
    SearchUiType.NONE -> {}
    SearchUiType.EMPTY_RESULT -> {}
    SearchUiType.LOADED_RESULT -> {
      val listState = rememberLazyListState()
      PaginationLoadEffect(
        listState = listState,
        isEnd = uiState.isEndPage,
        bufferItemCount = 0,
        onScrollToEnd = onScrollToEnd
      )
      LazyColumn(
        modifier = modifier,
        state = listState
      ) {
        items(uiState.uiModels) { item ->
          Surface(
            shape = MaterialTheme.shapes.medium
          ){
            Text(
              modifier = Modifier
                .clickable(onClick = { onClickedItem(item) }),
              text = item.title,
              style = TextStyle(
                fontSize = 20.sp,
              )
            )
          }
        }
      }
    }
  }
}