package kr.co.architecture.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HOME_BASE_ROUTE = "firstBaseRoute"
fun NavGraphBuilder.homeScreen(
  onNavigateToAlimCenterScreen: () -> Unit = {}
) {
  composable(
    route = HOME_BASE_ROUTE
  ) {
    HomeScreen(
      onNavigateToAlimCenterScreen = onNavigateToAlimCenterScreen
    )
  }
}

@Composable
fun HomeScreen(
  onNavigateToAlimCenterScreen: () -> Unit = {}
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Button(
      modifier = Modifier
        .align(Alignment.Center),
      onClick = onNavigateToAlimCenterScreen
    ) {
      Text(
        modifier = Modifier
          .clickable(onClick = onNavigateToAlimCenterScreen),
        text = "알림센터 이동",
        fontSize = 30.sp
      )
    }
  }
}