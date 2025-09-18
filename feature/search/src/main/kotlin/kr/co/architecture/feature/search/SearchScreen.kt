package kr.co.architecture.feature.search

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
import kr.co.architecture.core.ui.noRippledClickable
import kr.co.architecture.core.ui.theme.CustomColors
import kr.co.architecture.core.ui.theme.CustomTypography
import kr.co.architecture.core.ui.theme.LocalCustomColors
import kr.co.architecture.core.ui.theme.LocalCustomShapes
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

      Column(
        modifier = modifier
          .padding(
            start = 20.dp,
            end = 20.dp
          )
      ) {
        var query by rememberSaveable { mutableStateOf("") }
        SearchBarField(
          modifier = Modifier,
          value = query,
          onValueChange = { query = it },
          placeholder = stringResource(coreUiR.string.placeHolder),
          onClickedErase = {
            println("clickLog")
          }
        )

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
          modifier = Modifier,
          contentPadding = PaddingValues(
            top = 30.dp,
            bottom = 56.dp
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
                    .size(22.dp),
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
}

@Composable
fun SearchBarField(
  modifier: Modifier = Modifier,
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String = "",
  onClickedErase: () -> Unit = {},
  enabled: Boolean = true,
  colors: CustomColors = LocalCustomColors.current,
  typo: CustomTypography = LocalCustomTypography.current,
  shape: RoundedCornerShape = LocalCustomShapes.current.shape,
  @DrawableRes leadingIconRes: Int = coreUiR.drawable.icon_search,
  @DrawableRes trailingIconRes: Int = coreUiR.drawable.icon_delete
) {
  BasicTextField(
    modifier = modifier
      .clip(shape)
      .background(colors.searchBackground)
      /**
       * 과제 요구사항에 width를 335dp로 설정하라 나와있지만
       * 이는 `Configuration Change`에 대응을 못한다 판단되어 진행하지 않았습니다.
       */
      .height(54.dp)
      .padding(vertical = 15.dp)
      .padding(start = 20.dp, end = 18.dp),
    value = value,
    onValueChange = onValueChange,
    singleLine = true,
    enabled = enabled,
    textStyle = typo.searchContents,
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions(onSearch = { defaultKeyboardAction(ImeAction.Search) }),
    decorationBox = { innerTextField ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Leading icon
        Image(
          modifier = Modifier
            .size(24.dp),
          painter = painterResource(leadingIconRes),
          contentDescription = null,
        )

        // Input + PlaceHolder
        Box(
          modifier = Modifier
            .weight(1f),
          contentAlignment = Alignment.CenterStart
        ) {
          if (value.isEmpty()) {
            BasicText(
              text = placeholder,
              style = typo.searchMedium
            )
          }
          innerTextField()
        }

        // Trading icon
        Box(
          modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .noRippledClickable(
              onClick = { onClickedErase() }
            ),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(trailingIconRes),
            contentDescription = null
          )
        }
      }
    }
  )
}
