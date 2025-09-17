package kr.co.architecture.core.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlin.collections.lastOrNull

@Composable
fun PaginationLoadEffect(
  listState: LazyListState,
  isEnd: Boolean,
  onScrollToEnd: () -> Unit,
  bufferItemCount: Int = 5
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
    if (shouldLoadMore && !isEnd) onScrollToEnd()
  }
}