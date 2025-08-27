package kr.co.architecture.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.Dispatchers
import kr.co.architecture.core.ui.CoilAsyncImage
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.HtmlText
import kr.co.architecture.core.ui.roundItem
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
        is HomeUiSideEffect.Load -> viewModel.fetchData()
      }
    }
  }

  SearchScreen(
    modifier = modifier,
    uiState = uiState,
    onClickedItem = { viewModel.setEvent(SearchUiEvent.OnClickedItem(it)) }
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  uiState: SearchUiState,
  onClickedItem: (UiModel) -> Unit = {}
) {

  when (uiState.uiType) {
    SearchUiType.NONE -> {}
    SearchUiType.LOADED -> {
      LazyColumn(
        modifier = modifier
          .background(Color.LightGray)
      ) {
        items(
          items = uiState.uiModels,
          key = { it.id }
        ) { item ->
          BookItem(
            modifier = Modifier
              .padding(10.dp),
            uiModel = item
          )
        }
      }
    }
  }
}

@Composable
fun BookItem(
  modifier: Modifier = Modifier,
  uiModel: UiModel
) {
  Row(
    modifier = modifier
      .roundItem(roundDp = 10.dp)
      .padding(10.dp)
      .fillMaxWidth()
      .height(IntrinsicSize.Max)
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
        .weight(0.1f),
      painter = painterResource(id = coreUiR.drawable.ic_bookmark_filled),
      contentDescription = null
    )
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