package kr.co.architecture.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import kr.co.architecture.core.ui.EmptyResultContent
import kr.co.architecture.core.ui.NoMaterial3SearchBarTextField
import kr.co.architecture.core.ui.NoResultContent
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.baseClickable
import kr.co.architecture.core.ui.theme.LocalCustomColors
import kr.co.architecture.core.ui.theme.LocalCustomTypography
import kr.co.architecture.core.ui.util.asString
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
    onQueryChange = { viewModel.setEvent(SearchUiEvent.OnQueryChange(it)) },
    onSearch = { viewModel.setEvent(SearchUiEvent.OnSearch) },
    onClickedBookmark = { viewModel.setEvent(SearchUiEvent.OnClickedBookmark(it)) },
    onClickedItem = { viewModel.setEvent(SearchUiEvent.OnClickedItem(it)) },
    onScrollToEnd = { viewModel.setEvent(SearchUiEvent.OnScrolledToEnd) }
  )
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  uiState: SearchUiState,
  onQueryChange: (String) -> Unit = {},
  onSearch: () -> Unit = {},
  onClickedBookmark: (UiModelState.ContentsUiModel) -> Unit = { },
  onClickedItem: (UiModelState.ContentsUiModel) -> Unit = {},
  onScrollToEnd: () -> Unit
) {

  // TODO: vectorImage 모두 사용했는지?
  Column(
    modifier = modifier
      .padding(
        top = 20.dp,
        start = 20.dp,
        end = 20.dp
      ),
    verticalArrangement = Arrangement.spacedBy(30.dp)
  ) {
    NoMaterial3SearchBarTextField(
      modifier = Modifier,
      onValueChange = onQueryChange,
      placeholder = stringResource(coreUiR.string.placeHolder),
      onSearch = onSearch,
      onClickedErase = { onQueryChange("") }
    )
    when (uiState.uiType) {
      SearchUiType.NONE -> NoResultContent()
      SearchUiType.EMPTY_RESULT -> EmptyResultContent()
      SearchUiType.LOADED_RESULT -> {
        val listState = rememberLazyListState()
        PaginationLoadEffect(
          listState = listState,
          isEnd = uiState.isEndPage,
          bufferItemCount = 5,
          onScrollToEnd = onScrollToEnd
        )
        val typography = LocalCustomTypography.current
        LazyColumn(
          modifier = Modifier,
          contentPadding = PaddingValues(
            // TODO: 최 하단 56여백 주기
//            bottom = 56.dp
          ),
          verticalArrangement = Arrangement.spacedBy(24.dp),
          state = listState
        ) {
          items(
            items = uiState.uiModels
          ) { uiModel ->
            when (val uiModel = uiModel) {
              is UiModelState.ContentsUiModel -> {
                Row(
                  modifier = Modifier
                    .clickable(onClick = { onClickedItem(uiModel) }),
                  horizontalArrangement = Arrangement.spacedBy(10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  // TODO: Glide와 비교하여 기술선택
                  Box(
                    modifier = Modifier
                      .size(90.dp)
                  ) {
                    CoilAsyncImage(
                      url = uiModel.thumbnailUrl
                    )

                    Image(
                      modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset((-10).dp, 10.dp)
                        .size(22.dp)
                        .baseClickable { onClickedBookmark(uiModel) },
                      painter = painterResource(
                        id = when (uiModel.isBookmarked) {
                          true -> coreUiR.drawable.icon_like_on
                          false -> coreUiR.drawable.icon_like_off
                        }
                      ),
                      contentDescription = null
                    )
                  }

                  Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
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
                        text = uiModel.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.title
                      )

                      uiModel.collection?.let {
                        BasicText(
                          text = it,
                          style = typography.titleMedium
                        )
                      }
                    }

                    BasicText(
                      text = uiModel.contents,
                      style = typography.contents
                    )

                    Spacer(
                      modifier = Modifier.height(2.dp)
                    )

                    BasicText(
                      text = uiModel.dateTime,
                      style = typography.contentsMedium
                    )
                  }
                }
              }
              is UiModelState.PagingUiModel -> {
                Tail(uiModel.page.asString())
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun Tail(
  page: String
) {
  val colors = LocalCustomColors.current
  val typography = LocalCustomTypography.current
  Column {
    BasicText(
      modifier = Modifier
        .align(Alignment.CenterHorizontally),
      text = page,
      style = typography.title
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
        .height(1.dp)
        .background(colors.unselectedDivider)
    )
  }
}