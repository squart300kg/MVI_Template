package kr.co.architecture.app

import android.os.Bundle
import androidx.compose.ui.Modifier
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.app.ui.navigation.BaseNavigationBarWithItems
import kr.co.architecture.app.ui.theme.BaseTheme
import kr.co.architecture.feature.first.FIRST_BASE_ROUTE
import kr.co.architecture.feature.first.firstScreen
import kr.co.architecture.feature.second.secondScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      BaseTheme {
        val navHostController = rememberNavController()

        Scaffold(
          bottomBar = {
            BaseNavigationBarWithItems(navHostController)
          }
        ) { innerPadding ->
          CompositionLocalProvider() {
            NavHost(
              modifier = Modifier.padding(innerPadding),
              navController = navHostController,
              startDestination = FIRST_BASE_ROUTE
            ) {
              firstScreen()

              secondScreen()
            }
          }
        }
      }
    }
  }
}
