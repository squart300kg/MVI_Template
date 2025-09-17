package kr.co.architecture.feature.second

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.ui.SecondRoute
import kr.co.architecture.core.ui.util.asString

fun NavGraphBuilder.secondScreen() {
  composable<SecondRoute> {
    SecondScreen()
  }
}

@Composable
fun SecondScreen(
  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: SecondViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiSideEffect.collect { effect ->
        when (effect) {
          is SecondUiSideEffect.Load -> viewModel.fetchData()
        }
      }
    }
  }
  SecondScreen(
    uiState = uiState,
    modifier = modifier,
  )
}

@Composable
fun SecondScreen(
  modifier: Modifier = Modifier,
  uiState: SecondUiState,
) {

  when (uiState.uiType) {
    SecondUiType.NONE -> {}
    SecondUiType.LOADED -> {
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