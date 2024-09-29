package kr.co.architecture.ui.first

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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

const val FIRST_BASE_ROUTE = "firstBaseRoute"
const val SECOND_BASE_ROUTE = "secondBaseRoute"
fun NavGraphBuilder.firstScreen() {
    composable(
        route = FIRST_BASE_ROUTE
    ) {
        FirstScreen()
    }
}

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
        uiState = uiState,
        modifier = modifier,
    )

}

@Composable
fun FirstScreen(
    modifier: Modifier = Modifier,
    uiState: FirstUiState,
) {

    when (uiState.uiType) {
        FirstUiType.NONE -> {}
        FirstUiType.LOADED -> {
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