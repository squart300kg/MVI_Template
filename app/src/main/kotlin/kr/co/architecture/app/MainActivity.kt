package kr.co.architecture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.core.ui.BaseCenterDialog
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.core.ui.LocalOnErrorMessageChanged
import kr.co.architecture.core.ui.LocalOnLoadingStateChanged
import kr.co.architecture.core.ui.LocalOnRefreshStateChanged
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.feature.home.HOME_BASE_ROUTE
import kr.co.architecture.feature.home.homeScreen
import kr.co.architecture.feature.second.SECOND_BASE_ROUTE
import kr.co.architecture.feature.second.secondScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      BaseTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val navHostController = rememberNavController()

        Scaffold { innerPadding ->
          CompositionLocalProvider(
            LocalOnErrorMessageChanged provides { viewModel.showErrorDialog(it) },
            LocalOnLoadingStateChanged provides { viewModel.setLoadingState(it) },
            LocalOnRefreshStateChanged provides { viewModel.setRefreshState(it) }
          ) {
            NavHost(
              modifier = Modifier.padding(innerPadding),
              navController = navHostController,
              startDestination = HOME_BASE_ROUTE
            ) {
              homeScreen(
                onNavigateToAlimCenterScreen = {
                  navHostController.navigate(SECOND_BASE_ROUTE)
                }
              )

              secondScreen()
            }
          }

          BaseProgressBar(uiState.isLoading)

          uiState.errorDialog?.let { state ->
            BaseCenterDialog(
              baseCenterDialogUiModel = state,
              onClickedConfirm = {
                viewModel.setEvent(MainUiEvent.OnClickedErrorDialogConfirm)
              }
            )
          }
        }
      }
    }
  }
}
