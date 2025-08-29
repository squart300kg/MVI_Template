package kr.co.architecture.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.architecture.core.ui.CoilAsyncImage
import kr.co.architecture.core.ui.DetailRoute
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.HtmlText
import kr.co.architecture.core.ui.util.asString
import kr.co.architecture.core.ui.R as coreUiR

fun NavGraphBuilder.detailScreen() {
  composable<DetailRoute> {
    DetailScreen()
  }
}

@Composable
fun DetailScreen(
  modifier: Modifier = Modifier,
  viewModel: DetailViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.uiSideEffect.collect { effect ->
      when (effect) {
        is DetailUiSideEffect.Load -> viewModel.fetchData()
      }
    }
  }
  DetailScreen(
    modifier = modifier,
    uiState = uiState,
    onClickedBookmark = { viewModel.setEvent(DetailUiEvent.OnClickedBookmark) },
    onClickedBack = { viewModel.setEvent(DetailUiEvent.OnClickedBack) },
  )

  GlobalUiStateEffect(viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
  modifier: Modifier = Modifier,
  uiState: DetailUiState,
  onClickedBookmark: () -> Unit = {},
  onClickedBack: () -> Unit = {}
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(coreUiR.string.back))
        },
        navigationIcon = {
          IconButton(onClick = onClickedBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null
            )
          }
        },
        actions = {
          IconButton(onClick = onClickedBookmark) {
            Icon(
              imageVector =
                if (uiState.isBookmarked) Icons.Filled.Favorite
                else Icons.Outlined.FavoriteBorder,
              contentDescription = null
            )
          }
        }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .padding(16.dp)
        .fillMaxSize()
    ) {

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        CoilAsyncImage(
          modifier = Modifier
            .sizeIn(minWidth = 96.dp)
            .weight(0.35f, fill = false)
            .aspectRatio(0.75f),
          url = uiState.thumbnail
        )

        Spacer(Modifier.width(16.dp))

        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          HtmlText(inputText = uiState.publisher.asString())
          HtmlText(inputText = uiState.publishDate.asString())
          HtmlText(inputText = uiState.price.asString())
        }
      }

      Spacer(Modifier.height(20.dp))

      Spacer(Modifier.height(8.dp))

      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp
      ) {
        Text(
          text = uiState.contents.asString(),
          modifier = Modifier.padding(14.dp)
        )
      }
    }
  }
}