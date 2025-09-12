package kr.co.architecture.yeo.feature.search.fake

import kr.co.architecture.yeo.core.router.internal.navigator.Navigator
import kr.co.architecture.yeo.core.router.internal.navigator.Route


class FakeNavigator : Navigator {
  var latestNavigatingRoute: Route? = null
  var backCount = 0
  var webUrl: String? = null

  override suspend fun navigate(route: Route, saveState: Boolean, launchSingleTop: Boolean) {
    latestNavigatingRoute = route
  }
  override suspend fun navigateBack() { backCount++ }
  override suspend fun navigateWeb(url: String) { webUrl = url }
}