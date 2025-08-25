package kr.co.architecture.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.ui.GlobalUiStateEffect

const val DETAIL_BASE_ROUTE = "detailBaseRoute"
fun NavGraphBuilder.detailScreen() {
  composable(
    route = DETAIL_BASE_ROUTE
  ) {
    DetailScreen()
  }
}

@Composable
fun DetailScreen(
  modifier: Modifier = Modifier,
  viewModel: DetailViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is DetailUiSideEffect.Load -> viewModel.fetchData()
      }
    }
  }
  DetailScreen(
    uiState = uiState,
    modifier = modifier,
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun DetailScreen(
  modifier: Modifier = Modifier,
  uiState: DetailUiState,
) {

  when (uiState.uiType) {
    DetailUiType.NONE -> {}
    DetailUiType.LOADED -> { }
  }
}