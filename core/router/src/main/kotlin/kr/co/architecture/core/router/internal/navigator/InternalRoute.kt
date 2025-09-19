package kr.co.architecture.core.router.internal.navigator

internal sealed interface InternalRoute {
  data class Navigate(
    val route: Route,
    val saveState: Boolean,
    val launchSingleTop: Boolean
  ): InternalRoute
  data class NavigateWeb(val url: String): InternalRoute
  data object NavigateBack: InternalRoute
  data class NavigateDeepLink(val url: String, val extras: Map<String, String>) : InternalRoute
}
