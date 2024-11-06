package kr.co.architecture.app.ui.navigation

import androidx.annotation.DrawableRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.app.R
import kr.co.architecture.feature.first.FIRST_BASE_ROUTE
import kr.co.architecture.feature.first.SECOND_BASE_ROUTE

val baseDestinations: ImmutableList<BaseDestination> =
    BaseDestination.entries.toImmutableList()

enum class BaseDestination(
    @DrawableRes val selectedIconRes: Int,
    @DrawableRes val unselectedIconRes: Int,
    val iconTextIdRes: String,
    val route: String
) {
    FIRST(
        selectedIconRes = R.drawable.tab_first_on,
        unselectedIconRes = R.drawable.tab_first_off,
        iconTextIdRes = "first",
        route = FIRST_BASE_ROUTE
    ),
    SECOND(
        selectedIconRes = R.drawable.tab_second_on,
        unselectedIconRes = R.drawable.tab_second_off,
        iconTextIdRes = "second",
        route = SECOND_BASE_ROUTE
    )
}