package kr.co.architecture.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun BaseProgressBar(isLoading: Boolean) {
  if (isLoading) {
    Box(
      modifier = Modifier
          .fillMaxSize()
          .background(Color.Transparent)
    ) {
      val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading),
        onRetry = { _, _ -> isLoading }
      )
      val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
      )
      LottieAnimation(
        modifier = Modifier
            .size(150.dp)
            .align(Alignment.Center),
        composition = composition,
        progress = { progress },
        contentScale = ContentScale.Fit
      )
    }
  }
}