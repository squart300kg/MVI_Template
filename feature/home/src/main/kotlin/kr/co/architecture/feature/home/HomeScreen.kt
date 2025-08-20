package kr.co.architecture.feature.home

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

const val HOME_BASE_ROUTE = "firstBaseRoute"
const val SECOND_BASE_ROUTE = "secondBaseRoute"
fun NavGraphBuilder.homeScreen() {
  composable(
    route = HOME_BASE_ROUTE
  ) {
    HomeScreen()
  }
}

@Composable
fun HomeScreen(
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

  HomeScreen(
    uiState = uiState,
    modifier = modifier,
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  uiState: HomeUiState,
) {

  when (uiState.uiType) {
    HomeUiType.NONE -> {}
    HomeUiType.LOADED -> {
      LazyColumn(modifier) {
        items(uiState.uiModels) { item ->
          Text(
            modifier = Modifier
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(4.dp),
                    color = Color.LightGray
                )
                .padding(8.dp),
            text = item.name.asString(),
            style = TextStyle(
              fontSize = 20.sp,
            )
          )
        }
      }
    }
  }
}