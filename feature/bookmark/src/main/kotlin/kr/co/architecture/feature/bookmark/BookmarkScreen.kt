package kr.co.architecture.feature.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.ui.CoilAsyncImage
import kr.co.architecture.core.ui.NoResultContent
import kr.co.architecture.core.ui.baseClickable
import kr.co.architecture.core.ui.theme.LocalCustomTypography
import kr.co.architecture.core.ui.R as coreUiR

@Composable
fun BookmarkScreen(
  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: BookmarkViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.uiSideEffect.collect { effect ->
        when (effect) {
          is BookmarkUiSideEffect.OnStartDetailActivity -> {}
        }
      }
    }
  }
  BookmarkScreen(
    modifier = modifier,
    uiState = uiState,
    onClickedItem = { viewModel.setEvent(BookmarkUiEvent.OnClickedItem(it)) },
    onClickedBookmark = { viewModel.setEvent(BookmarkUiEvent.OnClickedBookmark(it)) },
  )
}

@Composable
fun BookmarkScreen(
  modifier: Modifier = Modifier,
  uiState: BookmarkUiState,
  onClickedItem: (UiModel) -> Unit = {},
  onClickedBookmark: (UiModel) -> Unit = {}
) {
  val gridState = rememberLazyGridState()

  when (uiState.uiType) {
    BookmarkUiType.NONE -> {}
    BookmarkUiType.EMPTY_RESULT -> NoResultContent(textRes = coreUiR.string.noBookmark)
    BookmarkUiType.LOADED_RESULT -> {}
  }

  LazyVerticalGrid(
    modifier = modifier,
    columns = GridCells.Fixed(2),
    state = gridState,
    contentPadding = PaddingValues(
//      start = 30.dp,
//      end = 30.dp,
//      top = 30.dp,
//      bottom = 30.dp
    ),
//    horizontalArrangement = Arrangement.spacedBy(30.dp),
//    verticalArrangement = Arrangement.spacedBy(40.dp)
  ) {
    items(
      items = uiState.uiModels
    ) { uiModel ->
      BookmarkGridItem(
        uiModel = uiModel,
        onClick = { onClickedItem(uiModel) },
        onClickedBookmark = { onClickedBookmark(uiModel) }
      )
    }
  }
}

@Composable
private fun BookmarkGridItem(
  uiModel: UiModel,
  onClick: () -> Unit = {},
  onClickedBookmark: (UiModel) -> Unit = {}
) {
  val typography = LocalCustomTypography.current
  Column(
    modifier = Modifier
      .clickable(onClick = onClick),
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Box(
      modifier = Modifier
        .size(150.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(12.dp))
    ) {
      CoilAsyncImage(
        modifier = Modifier.fillMaxSize(),
        url = uiModel.thumbnailUrl
      )

      // 우상단 북마크 버튼(하트)
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

    Spacer(
      modifier = Modifier.height(2.dp)
    )

    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painter = painterResource(
          when (uiModel.mediaContentsType) {
            MediaContentsTypeEnum.IMAGE -> coreUiR.drawable.icon_image
            MediaContentsTypeEnum.VIDEO -> coreUiR.drawable.icon_video
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
    }

    BasicText(
      text = uiModel.dateTime,
      style = typography.contentsMedium
    )
  }
}

// TODO: preview그리기