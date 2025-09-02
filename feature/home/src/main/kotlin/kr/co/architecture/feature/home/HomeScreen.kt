package kr.co.architecture.feature.home

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.architecture.core.ui.CoilAsyncImage
import kr.co.architecture.core.ui.PaginationLoadEffect
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is HomeUiSideEffect.Load -> viewModel.fetchData()
      }
    }
  }

  FirstScreen(
    modifier = modifier,
    uiState = uiState,
    onScrollToEnd = { viewModel.setEvent(HomeUiEvent.OnScrolledToEnd) }
  )
}

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  uiState: HomeUiState,
  onScrollToEnd: () -> Unit = {}
) {

  when (uiState.uiType) {
    HomeUiType.NONE -> {}
    HomeUiType.LOADED -> {
      val listState = rememberLazyGridState()
      PaginationLoadEffect(
        listState = listState,
        onScrollToEnd = onScrollToEnd,
      )
      LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        state = listState
      ) {
        items(uiState.uiModels) { item ->
          CoilAsyncImage(
            modifier = Modifier.aspectRatio(1f),
            url = item.image)
        }
      }
    }
  }
}