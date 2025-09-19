package kr.co.architecture.core.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kr.co.architecture.core.router.internal.viewmodel.RouteSideEffect
import kr.co.architecture.core.router.internal.viewmodel.RouterViewModel

// TODO: 필요없으면 navigation 모듈 삭제
@Composable
fun LaunchedRouter(
  navHostController: NavHostController = rememberNavController(),
  uriHandler: UriHandler = LocalUriHandler.current,
) {
  InternalLaunchedRouter(
    navHostController = navHostController,
    uriHandler = uriHandler
  )
}

@Composable
private fun InternalLaunchedRouter(
  navHostController: NavHostController,
  uriHandler: UriHandler,
  routerViewModel: RouterViewModel = hiltViewModel(),
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  LaunchedEffect(routerViewModel, lifecycleOwner) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      routerViewModel.sideEffect.collectLatest { sideEffect ->
        when (sideEffect) {
          is RouteSideEffect.NavigateBack -> {
            navHostController.popBackStack()
          }

          is RouteSideEffect.NavigateWeb -> {
            uriHandler.openUri(sideEffect.url)
          }

          is RouteSideEffect.Navigate -> {
            navHostController.navigate(sideEffect.route) {
              if (sideEffect.saveState) {
                navHostController.graph.findStartDestination().route?.let {
                  popUpTo(it) {
                    saveState = true
                  }
                }
                restoreState = true
              }
              launchSingleTop = sideEffect.launchSingleTop
            }
          }
        }
      }
    }
  }
}
