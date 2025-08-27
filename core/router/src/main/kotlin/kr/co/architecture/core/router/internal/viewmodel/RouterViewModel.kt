package kr.co.architecture.core.router.internal.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kr.co.architecture.core.router.internal.navigator.InternalNavigator
import kr.co.architecture.core.router.internal.navigator.InternalRoute
import javax.inject.Inject

@HiltViewModel
internal class RouterViewModel @Inject constructor(
  navigator: InternalNavigator,
) : ViewModel() {

  val sideEffect by lazy(LazyThreadSafetyMode.NONE) {
    navigator.channel.receiveAsFlow()
      .map { router ->
        when (router) {
          is InternalRoute.Navigate -> RouteSideEffect.Navigate(
            router.route,
            router.saveState,
            router.launchSingleTop,
          )

          is InternalRoute.NavigateWeb -> RouteSideEffect.NavigateWeb(router.url)

          is InternalRoute.NavigateBack -> RouteSideEffect.NavigateBack
        }
      }
  }
}
