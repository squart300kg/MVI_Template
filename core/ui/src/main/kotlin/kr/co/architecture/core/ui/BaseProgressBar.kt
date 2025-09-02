package kr.co.architecture.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerDefaults.scrimColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseProgressBar(isLoading: Boolean) {
  if (!isLoading) return

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(scrimColor),
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator(
      modifier = Modifier.size(36.dp),
      color = MaterialTheme.colorScheme.primary,
      strokeWidth = 4.dp
    )
  }
}