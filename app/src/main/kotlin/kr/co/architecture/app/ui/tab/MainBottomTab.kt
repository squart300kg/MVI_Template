package kr.co.architecture.app.ui.tab

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import kr.co.architecture.app.R
import kr.co.architecture.core.router.internal.navigator.Route
import kr.co.architecture.core.ui.SearchRoute
import kr.co.architecture.core.ui.BookmarkRoute

enum class MainBottomTab(
  @StringRes val iconTextIdRes: Int,
  val route: Route
) {
  FIRST(
    iconTextIdRes = R.string.search,
    route = SearchRoute
  ),
  SECOND(
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