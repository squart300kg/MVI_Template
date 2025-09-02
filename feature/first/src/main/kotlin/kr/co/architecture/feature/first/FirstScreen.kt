package kr.co.architecture.feature.first

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import kr.co.architecture.core.ui.util.asString

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  viewModel: FirstViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is FirstUiSideEffect.Load -> viewModel.fetchData()
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
  uiState: FirstUiState
) {

  when (uiState.uiType) {
    FirstUiType.NONE -> {}
    FirstUiType.LOADED -> {
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