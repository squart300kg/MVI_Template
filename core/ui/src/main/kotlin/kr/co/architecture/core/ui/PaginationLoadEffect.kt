package kr.co.architecture.core.ui

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun PaginationLoadEffect(
  listState: LazyGridState,
  onScrollToEnd: () -> Unit,
  hasNext: Boolean,
  bufferItemCount: Int = 20
) {
  val shouldLoadMore by remember {
    derivedStateOf {
      val layoutInfo = listState.layoutInfo
      val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
      val totalItemCount = layoutInfo.totalItemsCount

      totalItemCount != 0 &&
        lastVisibleItemIndex >= totalItemCount - bufferItemCount
    }
  }

  LaunchedEffect(shouldLoadMore) {
    if (shouldLoadMore && hasNext) onScrollToEnd()
  }
}