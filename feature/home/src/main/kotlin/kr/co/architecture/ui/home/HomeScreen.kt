package kr.co.architecture.ui.home

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
import kr.co.architecture.core.model.UiResult

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.list.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
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