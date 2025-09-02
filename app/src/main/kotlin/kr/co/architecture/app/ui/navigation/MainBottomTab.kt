package kr.co.architecture.app.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import kr.co.architecture.app.R
import kr.co.architecture.core.router.internal.navigator.Route
import kr.co.architecture.core.ui.FirstRoute
import kr.co.architecture.core.ui.SecondRoute

enum class MainBottomTab(
  @DrawableRes val selectedIconRes: Int,
  @DrawableRes val unselectedIconRes: Int,
  @StringRes val iconTextIdRes: Int,
  val route: Route
) {
  FIRST(
    selectedIconRes = R.drawable.tab_first_on,
    unselectedIconRes = R.drawable.tab_first_off,
    iconTextIdRes = R.string.first,
    route = FirstRoute
  ),
  SECOND(
    selectedIconRes = R.drawable.tab_second_on,
    unselectedIconRes = R.drawable.tab_second_off,
    iconTextIdRes = R.string.second,
    route = SecondRoute
  );

  companion object {
    @Composable
    fun find(predicate: @Composable (Route) -> Boolean): MainBottomTab? {
      return entries.find { predicate(it.route) }
    }

    @Composable
    fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
      return entries.map { it.route }.any { predicate(it) }
    }
  }
}