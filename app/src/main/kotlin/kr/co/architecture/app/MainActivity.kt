package kr.co.architecture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kr.co.architecture.app.ui.navigation.BaseNavigationBarWithItems
import kr.co.architecture.app.ui.navigation.MainNavigator
import kr.co.architecture.app.ui.navigation.rememberMainNavigator
import kr.co.architecture.core.router.LaunchedRouter
import kr.co.architecture.core.ui.BaseCenterDialog
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.core.ui.GlobalUiBus
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.feature.bookmark.bookmarkScreen
import kr.co.architecture.feature.detail.detailScreen
import kr.co.architecture.feature.search.searchScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var globalUiBus: GlobalUiBus

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val loadingState by globalUiBus.loadingState.collectAsStateWithLifecycle()
      val errorMessageState by globalUiBus.errorDialog.collectAsStateWithLifecycle()
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
          // TODO: 네트워크 처리 & 네트워크 에러 다이얼로그 로딩시 무제한 GC처리 후 죽음
          content =  { innerPadding ->
            NavHost(
              modifier = Modifier.padding(innerPadding),
              navController = navigator.navController,
              startDestination = navigator.startDestination
            ) {
              searchScreen()

              bookmarkScreen()

              detailScreen()
            }

            BaseProgressBar(loadingState)

            errorMessageState?.let { errorUiState ->
              BaseCenterDialog(
                baseCenterDialogUiModel = errorUiState,
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
