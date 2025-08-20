package kr.co.architecture.feature.alimCenter

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

const val ALIM_CENTER_BASE_ROUTE = "alimCenterBaseRoute"
fun NavGraphBuilder.secondScreen() {
  composable(
    route = ALIM_CENTER_BASE_ROUTE
  ) {
    AlimCenterScreen()
  }
}

@Composable
fun AlimCenterScreen(
  modifier: Modifier = Modifier,
  viewModel: AlimCenterViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is AlimCenterUiSideEffect.Load -> viewModel.fetchData()
      }
    }
  }
  AlimCenterScreen(
    uiState = uiState,
    modifier = modifier,
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun AlimCenterScreen(
  modifier: Modifier = Modifier,
  uiState: AlimCenterUiState,
) {

  when (uiState.uiType) {
    AlimCenterUiType.NONE -> {}
    AlimCenterUiType.LOADED -> {
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