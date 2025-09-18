package kr.co.architecture.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kr.co.architecture.core.model.ContentsType
import kr.co.architecture.core.ui.CoilAsyncImage
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.theme.LocalCustomTypography
import kr.co.architecture.core.ui.R as coreUiR

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

  // TODO: 엣지투엣지 대응
  // TODO: vectorImage 모두 사용했는지?
  when (uiState.uiType) {
    SearchUiType.NONE -> {}
    SearchUiType.EMPTY_RESULT -> {}
    SearchUiType.LOADED_RESULT -> {
      val listState = rememberLazyListState()
      PaginationLoadEffect(
        listState = listState,
        isEnd = uiState.isEndPage,
        bufferItemCount = 1,
        onScrollToEnd = onScrollToEnd
      )
      // TODO: Text들 간격조정, MaterialTheme로 강제화되는것 확인
      val typography = LocalCustomTypography.current
      LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
          top = 30.dp,
          bottom = 56.dp,
          start = 20.dp,
          end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        state = listState
      ) {
        itemsIndexed(uiState.uiModels) { index, uiModel ->
          Row(
            modifier = Modifier
              .clickable(onClick = { onClickedItem(uiModel) }),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // TODO: Glide와 비교하여 기술선택
            CoilAsyncImage(
              modifier = Modifier
                .size(90.dp),
              url = uiModel.thumbnailUrl
            )

            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Image(
                  modifier = Modifier.size(20.dp),
                  painter = painterResource(
                    id = when (uiModel.contentsType) {
                      ContentsType.VIDEO -> coreUiR.drawable.icon_video
                      ContentsType.IMAGE -> coreUiR.drawable.icon_image
                    }
                  ),
                  contentDescription = null
                )

                BasicText(
                  modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                  text = uiModel.title,
                  style = typography.title
                )

                uiModel.collection?.let {
                  BasicText(
                    text = it,
                    style = typography.titleMedium
                  )
                }
              }

              // TODO: 간격 설정
              Spacer(
                modifier = Modifier.height(6.dp)
              )

              BasicText(
                text = uiModel.contents,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = typography.contents
              )

              Spacer(
                modifier = Modifier.height(8.dp)
              )

              BasicText(
                text = uiModel.dateTime,
                style = typography.contentsMedium
              )
            }
          }

          if (index == uiState.uiModels.lastIndex) {
            BasicText(
              text = "${uiState.page}"
            )
          }
        }
      }
    }
  }
}