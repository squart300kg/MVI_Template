package kr.co.architecture.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.model.ContentsType
import kr.co.architecture.core.ui.CoilAsyncImage
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.R as coreUiR

fun NavGraphBuilder.searchScreen() {
  composable<SearchRoute> {
    SearchScreen()
  }
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: SearchViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiSideEffect.collect { effect ->
        when (effect) {
          is SearchUiSideEffect.Load -> viewModel.fetchData(effect)
        }
      }
    }
  }

  SearchScreen(
    modifier = modifier,
    uiState = uiState,
    onClickedItem = {},
    onScrollToEnd = { viewModel.setEvent(SearchUiEvent.OnScrolledToEnd) }
  )
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  uiState: SearchUiState,
  onClickedItem: (UiModel) -> Unit = {},
  onScrollToEnd: () -> Unit
) {

  // TODO: vectorImage 모두 사용했는지?
  when (uiState.uiType) {
    SearchUiType.NONE -> {}
    SearchUiType.EMPTY_RESULT -> {}
    SearchUiType.LOADED_RESULT -> {
      val listState = rememberLazyListState()
      PaginationLoadEffect(
        listState = listState,
        isEnd = uiState.isEndPage,
        bufferItemCount = 0,
        onScrollToEnd = onScrollToEnd
      )
      LazyColumn(
        modifier = modifier,
        state = listState
      ) {
        items(uiState.uiModels) { uiModel ->
          Surface(
            modifier = Modifier
              .clickable(onClick = { onClickedItem(uiModel) }),
            shape = MaterialTheme.shapes.medium
          ) {
            Row {
              CoilAsyncImage(
                modifier = Modifier,
                url = uiModel.thumbnailUrl
              )

              Column {
                Image(
                  painter = painterResource(
                    id = when (uiModel.contentsType) {
                      ContentsType.VIDEO -> coreUiR.drawable.icon_video
                      ContentsType.IMAGE -> coreUiR.drawable.icon_image
                    }
                  ),
                  contentDescription = null
                )

                Row {
                  Text(
                    text = uiModel.title,
                    style = MaterialTheme.typography.bodyLarge
                  )

                  uiModel.collection?.let {
                    Text(
                      text = it,
                      style = MaterialTheme.typography.bodySmall
                    )
                  }
                }

                Text(
                  text = uiModel.contents,
                  style = MaterialTheme.typography.bodyMedium
                )

                Text(
                  text = uiModel.dateTime,
                  style = MaterialTheme.typography.bodySmall
                )
              }
            }
          }
        }
      }
    }
  }
}