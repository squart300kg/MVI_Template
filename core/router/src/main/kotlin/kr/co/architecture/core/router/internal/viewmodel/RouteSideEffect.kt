package kr.co.architecture.core.router.internal.viewmodel

import kr.co.architecture.core.router.internal.navigator.Route

internal sealed interface RouteSideEffect {

  data class Navigate(
    val route: Route,
    val saveState: Boolean,
    val launchSingleTop: Boolean,
  ) : RouteSideEffect

  data class NavigateWeb(val url: String) : RouteSideEffect

  data object NavigateBack : RouteSideEffect
}
