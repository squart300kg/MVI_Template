package kr.co.architecture.ui.second

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val SECOND_BASE_ROUTE = "secondBaseRoute"
fun NavGraphBuilder.secondScreen() {
    composable(
        route = SECOND_BASE_ROUTE
    ) {
        SecondScreen()
    }
}

@Composable
fun SecondScreen(
    modifier: Modifier = Modifier,
    viewModel: SecondViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.uiSideEffect.collect { effect ->
            when (effect) {
                is SecondUiSideEffect.Load -> viewModel.fetchData()
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
                        text = item.name,
                        style = TextStyle(
                            fontSize = 20.sp,
                        )
                    )
                }
            }
        }
    }
}