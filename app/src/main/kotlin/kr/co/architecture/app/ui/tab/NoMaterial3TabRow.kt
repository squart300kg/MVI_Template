package kr.co.architecture.app.ui.tab

import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kr.co.architecture.app.R
import kr.co.architecture.core.ui.theme.CustomColors
import kr.co.architecture.core.ui.theme.CustomTypography
import kr.co.architecture.core.ui.theme.LocalCustomColors
import kr.co.architecture.core.ui.theme.LocalCustomTypography
import kr.co.architecture.core.ui.noRippledClickable
import kotlin.math.roundToInt

enum class MainTabEnum(
  val tabIndex: Int,
  @StringRes val tabStringRes: Int
) {
  SEARCH(0, R.string.search),
  BOOKMARK(1, R.string.bookmark);

  companion object {
    fun from(tabIndex: Int): MainTabEnum {
      return entries.firstOrNull() { it.tabIndex == tabIndex } ?: SEARCH
    }
  }
}

@Composable
fun NoMaterial3TabRow(
  modifier: Modifier = Modifier,
  selectedTab: MainTabEnum,
  onSelectedTabChanced: (MainTabEnum) -> Unit = {},
  colors: CustomColors = LocalCustomColors.current,
  typography: CustomTypography = LocalCustomTypography.current,
  density: Density = LocalDensity.current
) {
  val tabCount = MainTabEnum.entries.size
  val thinPx  = with(density) { 0.5.dp.toPx() }
  val animatedIndex by animateFloatAsState(
    targetValue = selectedTab.tabIndex.toFloat(),
    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .height(70.dp)
      .drawBehind {
        // 1) 회색 끝선 (전폭, 패딩 반영)
        val y = size.height - thinPx / 2f
        drawLine(
          color = colors.unselectedDivider,
          start = Offset(0f, y),
          end   = Offset(size.width, y),
          strokeWidth = thinPx
        )
      }
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(11.dp)
  ) {
    // 라벨 영역
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      verticalAlignment = Alignment.Bottom
    ) {
      MainTabEnum.entries.forEachIndexed { index, tab ->
        val selected = index == selectedTab.tabIndex
        Column(
          modifier = Modifier
            .weight(1f)
            .noRippledClickable(
              onClick = { onSelectedTabChanced(MainTabEnum.from(index)) }
            ),
          verticalArrangement = Arrangement.spacedBy(11.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          BasicText(
            text = stringResource(tab.tabStringRes),
            style = if (selected) typography.tab else typography.tabMedium
          )
        }
      }
    }

    Canvas(
      modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
    ) {
      if (tabCount > 0) {
        val segmentWidth = size.width / tabCount
        val left  = (segmentWidth * animatedIndex).roundToInt().toFloat()
        val right = (segmentWidth * (animatedIndex + 1f)).roundToInt().toFloat()
        val width = (right - left).coerceAtLeast(1f)

        drawRect(
          color = colors.selectedDivider,
          topLeft = Offset(left, 0f),
          size = Size(width = width, height = size.height)
        )
      }
    }
  }
}