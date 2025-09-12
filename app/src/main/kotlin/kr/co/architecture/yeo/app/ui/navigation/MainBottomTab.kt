package kr.co.architecture.yeo.app.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import kr.co.architecture.yeo.app.R
import kr.co.architecture.yeo.core.router.internal.navigator.Route
import kr.co.architecture.yeo.core.ui.SearchRoute
import kr.co.architecture.yeo.core.ui.BookmarkRoute

enum class MainBottomTab(
  @DrawableRes val selectedIconRes: Int,
  @DrawableRes val unselectedIconRes: Int,
  @StringRes val iconTextIdRes: Int,
  val route: Route
) {
  FIRST(
    selectedIconRes = R.drawable.tab_first_on,
    unselectedIconRes = R.drawable.tab_first_off,
    iconTextIdRes = R.string.search,
    route = SearchRoute
  ),
  SECOND(
    selectedIconRes = R.drawable.tab_second_on,
    unselectedIconRes = R.drawable.tab_second_off,
    iconTextIdRes = R.string.bookmark,
    route = BookmarkRoute
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