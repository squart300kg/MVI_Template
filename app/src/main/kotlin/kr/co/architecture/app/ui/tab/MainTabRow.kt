package kr.co.architecture.app.ui.tab

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.app.R

enum class MainTabEnum(
  val tabIndex: Int,
  @StringRes val tabStringRes: Int
) {
  SEARCH(0, R.string.search),
  BOOKMARK(1, R.string.bookmark);

  companion object {
    fun from(tabIndex: Int): MainTabEnum {
      return entries.firstOrNull() { it.tabIndex == tabIndex } ?: SEARCH
    }
  }
}

@Composable
fun MainTabRow(
  modifier: Modifier = Modifier,
  tabTextList: ImmutableList<String> = MainTabEnum.entries
    .map { stringResource(it.tabStringRes )}
    .toImmutableList(),
  selectedTabIndex: Int,
  onSelectedTabChanced: (mainTabEnum: MainTabEnum) -> Unit = {}
) {
  TabRow(
    modifier = modifier
      .fillMaxWidth(),
    containerColor = Color.White,
    selectedTabIndex = selectedTabIndex,
    indicator = { tabPositions ->
      TabRowDefaults.SecondaryIndicator(
        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
        color = Color.Black
      )
    }
  ) {
    tabTextList.forEachIndexed { index, title ->
      Tab(
        // TODO: 치수 재할당
        modifier = Modifier.height(50.dp),
        selected = selectedTabIndex == index,
        onClick = { onSelectedTabChanced(MainTabEnum.from(index)) },
        unselectedContentColor = Color.LightGray,
        selectedContentColor = Color.Black
      ) {
        BasicText(
          text = title
        )
      }
    }
  }
}