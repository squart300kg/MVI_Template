package kr.co.architecture.app.ui.navigation

import androidx.annotation.DrawableRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.app.R
import kr.co.architecture.core.router.internal.navigator.Route
import kr.co.architecture.feature.first.FirstRoute
import kr.co.architecture.feature.second.SecondRoute

enum class MainBottomTab(
  @DrawableRes val selectedIconRes: Int,
  @DrawableRes val unselectedIconRes: Int,
  val iconTextIdRes: String,
  val route: Route
) {
  FIRST(
    selectedIconRes = R.drawable.tab_first_on,
    unselectedIconRes = R.drawable.tab_first_off,
    iconTextIdRes = "first",
    route = FirstRoute
  ),
  SECOND(
    selectedIconRes = R.drawable.tab_second_on,
    unselectedIconRes = R.drawable.tab_second_off,
    iconTextIdRes = "second",
    route = SecondRoute
  )
}