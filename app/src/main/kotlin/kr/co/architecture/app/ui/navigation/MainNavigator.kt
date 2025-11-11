package kr.co.architecture.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

internal class MainNavigator(
  val navController: NavHostController,
) {
  private val currentDestination: NavDestination?
    @Composable get() = navController
      .currentBackStackEntryAsState().value?.destination

  val startDestination = MainBottomTab.FIRST.route

  val currentTab: MainBottomTab?
    @Composable get() = MainBottomTab.find { tab ->
      currentDestination?.hasRoute(tab::class) == true
    }

  @Composable
  fun shouldShowBottomBar() = MainBottomTab.contains {
    currentDestination?.hasRoute(it::class) == true
  }
}

@Composable
internal fun rememberMainNavigator(
  navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
  MainNavigator(navController)
}