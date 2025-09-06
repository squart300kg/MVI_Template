package kr.co.architecture.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.core.ui.ImageLoadingFailure
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.custom.image.loader.ui.AsyncImage

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  windowInfo: WindowInfo =  LocalWindowInfo.current,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val requestSize by remember {
    mutableIntStateOf(windowInfo.containerSize.width / 2)
  }
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is HomeUiSideEffect.Load -> viewModel.fetchData(requestSize)
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
        hasNext = uiState.hasNext
      )
      LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        state = listState
      ) {
        items(uiState.uiModels) { item ->
          Surface(
            modifier = Modifier
              .aspectRatio(1f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
            shape = RoundedCornerShape(4.dp)
          ) {
            AsyncImage(
              url = item.image,
              enableMemoryCache = true,
              enableDiskCache = true,
              loadingPlaceholderContent = {
                BaseProgressBar(true)
              },
              // TODO: 에러 종류에 따라 실패 이미지를 다르게 보여준다.
              // TODO: 재시도 누를 수 있도록 ui 구성
              errorPlaceholderContent = {
                ImageLoadingFailure()
              }
            )
          }
        }
      }
    }
  }
}