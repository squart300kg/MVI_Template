package kr.co.architecture.feature.detail

import androidx.compose.foundation.border
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
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.util.asString

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