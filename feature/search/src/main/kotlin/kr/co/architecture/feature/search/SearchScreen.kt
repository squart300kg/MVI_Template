package kr.co.architecture.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.ui.CoilAsyncImage
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.HtmlText
import kr.co.architecture.core.ui.PaginationLoadEffect
import kr.co.architecture.core.ui.baseClickable
import kr.co.architecture.core.ui.util.asString
import kr.co.architecture.core.ui.R as coreUiR

fun NavGraphBuilder.searchScreen() {
  composable<SearchRoute> {
    SearchScreen()
  }
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  viewModel: SearchViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is SearchUiSideEffect.Load -> {
          viewModel.fetchData(effect)
        }
      }
    }
  }

  SearchScreen(
    modifier = modifier,
    uiState = uiState,
    onQueryChange = { viewModel.setEvent(SearchUiEvent.OnQueryChange(it)) },
    onSearch = { viewModel.setEvent(SearchUiEvent.OnSearch) },
    onChangeSort = { viewModel.setEvent(SearchUiEvent.OnChangeSort(it)) },
    onClickedBookmark = { viewModel.setEvent(SearchUiEvent.OnClickedBookmark(it)) },
    onClickedItem = { viewModel.setEvent(SearchUiEvent.OnClickedItem(it)) },
    onScrollToEnd = { viewModel.setEvent(SearchUiEvent.OnScrolledToEnd) }
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  uiState: SearchUiState,
  onQueryChange: (String) -> Unit = {},
  onSearch: () -> Unit = {},
  onChangeSort: (SortTypeEnum) -> Unit = {},
  onClickedItem: (UiModel) -> Unit = {},
  onClickedBookmark: (UiModel) -> Unit = {},
  onScrollToEnd: () -> Unit = {}
) {
  val listState = rememberLazyListState()
  PaginationLoadEffect(
    listState = listState,
    isEnd = uiState.isPageable,
    onScrollToEnd = onScrollToEnd
  )

  Column(modifier = modifier.fillMaxSize()) {
    SearchHeader(
      query = { uiState.query },
      sort = uiState.sort,
      onQueryChange = onQueryChange,
      onSearch = onSearch,
      onChangeSort = onChangeSort
    )

    when (uiState.uiType) {
      SearchUiType.NONE -> {}
      SearchUiType.LOADED -> {
        LazyColumn(
          modifier = modifier
            .background(Color.LightGray),
          state = listState
        ) {
          items(
            items = uiState.uiModels,
            key = { it.isbn }
          ) { item ->
            BookItem(
              modifier = Modifier
                .padding(10.dp),
              uiModel = item,
              onClickedBookmark = onClickedBookmark,
              onClickedItem = onClickedItem
            )
          }
        }
      }
    }
  }
}

@Composable
fun BookItem(
  modifier: Modifier = Modifier,
  uiModel: UiModel,
  onClickedBookmark: (UiModel) -> Unit = {},
  onClickedItem: (UiModel) -> Unit = {}
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Max)
      .baseClickable { onClickedItem(uiModel) },
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier.padding(10.dp)
    ) {
      CoilAsyncImage(
        modifier = Modifier.weight(0.3f),
        url = uiModel.thumbnail,
      )

      Column(
        modifier = Modifier
          .weight(0.5f)
          .align(Alignment.CenterVertically),
      ) {
        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = stringResource(id = coreUiR.string.book),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.title.asString(),
          style = TextStyle(
            fontWeight = FontWeight.Bold
          ),
          maxLine = 2
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.publisher.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.authors.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        HtmlText(
          modifier = Modifier.padding(4.dp),
          inputText = uiModel.publishDate.asString(),
          style = TextStyle(
            fontSize = 12.sp
          )
        )

        Text(
          modifier = Modifier.padding(4.dp),
          text = uiModel.price.asString(),
          fontWeight = FontWeight.Bold
        )
      }

      Image(
        modifier = Modifier
          .wrapContentWidth(Alignment.End)
          .weight(0.1f)
          .baseClickable { onClickedBookmark(uiModel) },
        imageVector =
          if (uiModel.isBookmarked) Icons.Filled.Favorite
          else Icons.Outlined.FavoriteBorder,
        contentDescription = null
      )
    }
  }
}

@Composable
private fun sortLabel(sort: SortTypeEnum): String =
  when (sort) {
    SortTypeEnum.ACCURACY -> "정확도순"
    SortTypeEnum.LATEST -> "최신순"
  }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeader(
  modifier: Modifier = Modifier,
  query: () -> String = {""},
  sort: SortTypeEnum,
  onQueryChange: (String) -> Unit,
  onSearch: () -> Unit,
  onChangeSort: (SortTypeEnum) -> Unit
) {
  Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
    // 검색창
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp),
      value = query(),
      onValueChange = onQueryChange,
      placeholder = { Text("제목 또는 저자를 입력하세요.") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      trailingIcon = {
        if (query().isNotEmpty()) {
          IconButton(onClick = { onQueryChange("") }) {
            Icon(Icons.Outlined.Close, contentDescription = "지우기")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(24.dp),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )

    Spacer(Modifier.height(12.dp))

    // 정렬 표시 + 드롭다운
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(sortLabel(sort), style = MaterialTheme.typography.bodyMedium)

      var expanded by remember { mutableStateOf(false) }
      Box {
        AssistChip(
          onClick = { expanded = true },
          label = { Text("정렬") },
          leadingIcon = { Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null) }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
          DropdownMenuItem(
            text = { Text("정확도순") },
            onClick = { onChangeSort(SortTypeEnum.ACCURACY); expanded = false }
          )
          DropdownMenuItem(
            text = { Text("최신순") },
            onClick = { onChangeSort(SortTypeEnum.LATEST); expanded = false }
          )
        }
      }
    }
  }
}


//@Preview
//@Composable
//fun BookItemPreview() {
//  BookItem(
//    modifier = Modifier.background(Color.White),
//    uiModel = UiModel()
//  )
//}