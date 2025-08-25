package kr.co.architecture.app.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.router.internal.navigator.Route

@Composable
internal fun BaseNavigationBarWithItems(
  currentTab: MainBottomTab?,
  onClickedBottomTab: (MainBottomTab) -> Unit
) {
  NavigationBar(
    containerColor = Color.White
  ) {
    MainBottomTab.entries.forEach { destination ->
      BaseNavigationBarItem(
        onClick = { onClickedBottomTab(destination) },
        selected = currentTab == destination,
        destination = destination
      )
    }
  }
}

@Composable
fun RowScope.BaseNavigationBarItem(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  selected: Boolean,
  destination: MainBottomTab
) {
  NavigationBarItem(
    modifier = modifier,
    selected = selected,
    onClick = onClick,
    alwaysShowLabel = false,
    colors = NavigationBarItemDefaults.colors(
      indicatorColor = Color.Transparent
    ),
    icon = {
      Column {
        Image(
          modifier = Modifier
              .size(24.dp)
              .align(Alignment.CenterHorizontally),
          painter = painterResource(
            id = if (selected) {
              destination.selectedIconRes
            } else {
              destination.unselectedIconRes
            }
          ),
          contentDescription = null
        )

        Text(
          modifier = Modifier.align(Alignment.CenterHorizontally),
          text = destination.iconTextIdRes
        )
      }
    }
  )
}