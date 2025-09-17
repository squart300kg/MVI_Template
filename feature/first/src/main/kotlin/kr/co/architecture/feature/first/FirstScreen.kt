package kr.co.architecture.feature.first

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kr.co.architecture.core.ui.FirstRoute
import kr.co.architecture.core.ui.util.asString

fun NavGraphBuilder.firstScreen() {
  composable<FirstRoute> {
    FirstScreen()
  }
}

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: FirstViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiSideEffect.collect { effect ->
        when (effect) {
          is FirstUiSideEffect.Load -> viewModel.fetchData()
        }
      }
    }
  }

  FirstScreen(
    modifier = modifier,
    uiState = uiState,
    onClickedItem = { viewModel.setEvent(FirstUiEvent.OnClickedItem(it)) }
  )
}

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  uiState: FirstUiState,
  onClickedItem: (UiModel) -> Unit = {}
) {

  when (uiState.uiType) {
    FirstUiType.NONE -> {}
    FirstUiType.LOADED -> {
      LazyColumn(modifier) {
        items(uiState.uiModels) { item ->
          Surface(
            shape = MaterialTheme.shapes.medium
          ){
            Text(
              modifier = Modifier
                .padding(8.dp)
                .border(
                  width = 1.dp,
                  shape = RoundedCornerShape(4.dp),
                  color = Color.LightGray
                )
                .padding(8.dp)
                .clickable(onClick = { onClickedItem(item) }),
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
}