package kr.co.architecture.feature.first

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.architecture.core.ui.CoilAsyncImage

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
    uiState = uiState
  )
}

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  uiState: HomeUiState
) {

  when (uiState.uiType) {
    HomeUiType.NONE -> {}
    HomeUiType.LOADED -> {
      LazyColumn(modifier) {
        items(uiState.uiModels) { item ->
          CoilAsyncImage(url = item.image)
        }
      }
    }
  }
}