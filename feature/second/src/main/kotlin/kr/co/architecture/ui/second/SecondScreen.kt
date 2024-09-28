package kr.co.architecture.ui.second

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.model.UiResult

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
    val uiState by viewModel.list.collectAsStateWithLifecycle()
    FirstScreen(
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
fun FirstScreen(
    modifier: Modifier = Modifier,
    uiState: UiResult<UiModel>,
) {

    when (uiState) {
        is UiResult.Loading -> {}
        is UiResult.Error -> {}
        is UiResult.Empty -> {}
        is UiResult.Success -> {
            LazyColumn(modifier) {
                items(uiState.model.item) { item ->
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