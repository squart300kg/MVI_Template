package kr.co.architecture.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.DrawerDefaults.scrimColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImageLoadingFailure() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(scrimColor),
    contentAlignment = Alignment.Center
  ) {

    Image(
      imageVector = Icons.Outlined.Clear,
      contentDescription = null,
      modifier = Modifier.size(48.dp)
    )
  }
}