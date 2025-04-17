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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions

@Composable
internal fun BaseNavigationBarWithItems(
  navController: NavHostController
) {
  NavigationBar(
    containerColor = Color.White
  ) {
    baseDestinations.forEach { destination ->
      val selected = navController
        .getCurrentDestination()
        .isTopLevelDestinationInHierarchy(destination)

      BaseNavigationBarItem(
        onClick = {
          val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
              this.saveState = true
            }
            restoreState = true
            launchSingleTop = true
          }

          navController.navigate(destination.route, topLevelNavOptions)
        },
        selected = selected,
        destination = destination
      )
    }
  }
}

@Composable
fun NavHostController.getCurrentDestination(): NavDestination? {
  return this.currentBackStackEntryAsState()
    .value
    ?.destination
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: BaseDestination) =
  this?.hierarchy?.any {
    it.route?.contains(destination.route, true) ?: false
  } ?: false


@Composable
fun RowScope.BaseNavigationBarItem(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  selected: Boolean,
  destination: BaseDestination
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