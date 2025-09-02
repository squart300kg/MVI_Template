package kr.co.architecture.feature.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.ui.DetailRoute
import kr.co.architecture.core.ui.util.asString

fun NavGraphBuilder.detailScreen() {
  composable<DetailRoute> {
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
}

@Composable
fun DetailScreen(
  modifier: Modifier = Modifier,
  uiState: DetailUiState,
) {

  when (uiState.uiType) {
    DetailUiType.NONE -> {}
    DetailUiType.LOADED -> {
      Box(
        modifier = modifier.fillMaxSize()
      ) {
        Column(
          Modifier
            .align(Alignment.Center)
            .padding(10.dp)
            .border(
              width = 1.dp,
              color = Color.Gray
            )
            .padding(10.dp)
        ) {
          Text(
            text = "ID : ${uiState.id.asString()}"
          )
          Text(
            text = uiState.name.asString()
          )
        }
      }
    }
  }
}