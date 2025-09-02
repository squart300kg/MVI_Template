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
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.app.ui.navigation.BaseNavigationBarWithItems
import kr.co.architecture.app.ui.navigation.MainNavigator
import kr.co.architecture.app.ui.navigation.rememberMainNavigator
import kr.co.architecture.core.router.LaunchedRouter
import kr.co.architecture.core.ui.BaseCenterDialog
import kr.co.architecture.core.ui.BaseProgressBar
import kr.co.architecture.core.ui.LocalOnErrorMessageChanged
import kr.co.architecture.core.ui.LocalOnLoadingStateChanged
import kr.co.architecture.core.ui.LocalOnRefreshStateChanged
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.feature.detail.detailScreen
import kr.co.architecture.feature.first.firstScreen
import kr.co.architecture.feature.second.secondScreen

fun main() {
  Solution().run {
    println(solution(intArrayOf(30,30,30), arrayOf(intArrayOf(1,2,10), intArrayOf(2,3,20), intArrayOf(3,4,5), intArrayOf(3,4,30)), intArrayOf(1)).contentEquals(intArrayOf(0,20,15,55)))
  }
}


class Solution {
  fun solution(balance: IntArray, transaction: Array<IntArray>, abnormal: IntArray): IntArray {
    val userMoney = mutableListOf<MutableMap<String, Int>>()
    balance.forEachIndexed { index, i ->
      userMoney.apply {
        add(mutableMapOf<String, Int>().apply { put("$index", i) })
      }
    }

    transaction.forEachIndexed { index, singleTransaction ->
      val from = singleTransaction[0]
      val to = singleTransaction[1]
      val money = singleTransaction[2]

      val currentFromUserMoney = userMoney[0]
      
    }

    return intArrayOf(1)
  }
}














@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
            CompositionLocalProvider(
              LocalOnErrorMessageChanged provides { viewModel.showErrorDialog(it) },
              LocalOnLoadingStateChanged provides { viewModel.setLoadingState(it) },
              LocalOnRefreshStateChanged provides { viewModel.setRefreshState(it) }
            ) {
              NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navigator.navController,
                startDestination = navigator.startDestination
              ) {
                firstScreen()

                secondScreen()

                detailScreen()
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
        )
      }
    }
  }
}
