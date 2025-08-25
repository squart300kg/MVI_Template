package kr.co.architecture.core.router

import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

internal class NavigatorImpl @Inject constructor(): Navigator, InternalNavigator {
  override val channel = Channel<InternalRoute>(Channel.BUFFERED)

  override suspend fun navigate(route: Route, saveState: Boolean, launchSingleTop: Boolean) {
    channel.send(
      InternalRoute.Navigate(
        route = route,
        saveState = saveState,
        launchSingleTop = launchSingleTop,
      )
    )
  }

  override suspend fun navigateWeb(url: String) {
    channel.send(InternalRoute.NavigateWeb(url = url))
  }

  override suspend fun navigateBack() {
    channel.send(InternalRoute.NavigateBack)
  }
}