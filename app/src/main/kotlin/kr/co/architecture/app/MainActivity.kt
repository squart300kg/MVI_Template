package kr.co.architecture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.app.ui.navigation.BaseNavigationBarWithItems
import kr.co.architecture.app.ui.navigation.MainNavigator
import kr.co.architecture.app.ui.navigation.rememberMainNavigator
import kr.co.architecture.core.router.LaunchedRouter
import kr.co.architecture.core.ui.BaseCenterDialog
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.feature.detail.detailScreen
import kr.co.architecture.feature.first.firstScreen
import kr.co.architecture.feature.second.secondScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val loadingState by viewModel.globalUiBus.loadingState.collectAsStateWithLifecycle()
      val errorMessageState by viewModel.globalUiBus.failureDialog.collectAsStateWithLifecycle()
      val navigator: MainNavigator = rememberMainNavigator()

      LaunchedRouter(navigator.navController)

      BaseTheme {
        Scaffold(
          bottomBar = {
            BaseNavigationBarWithItems(
              currentTab = navigator.currentTab,
              visible = navigator.shouldShowBottomBar(),
              onClickedBottomTab = { selectedTab ->
                viewModel.setEvent(MainUiEvent.OnClickedBottomTab(selectedTab))
              }
            )
          },
          content =  { innerPadding ->
            NavHost(
              modifier = Modifier.padding(innerPadding),
              navController = navigator.navController,
              startDestination = navigator.startDestination
            ) {
              firstScreen()

              secondScreen()

              detailScreen()
            }

            BaseProgressBar(loadingState)

            errorMessageState?.let { state ->
              BaseCenterDialog(
                baseCenterDialogUiModel = state,
                onClickedConfirm = {
                  viewModel.setEvent(MainUiEvent.OnClickedErrorDialogConfirm)
                }
              )
            }
          }
        )
      }
    }
  }
}
