package kr.co.architecture.app.ui.tab

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kr.co.architecture.app.R
import kr.co.architecture.app.ui.theme.CustomColors
import kr.co.architecture.app.ui.theme.CustomTypography
import kr.co.architecture.app.ui.theme.LocalCustomColors
import kr.co.architecture.app.ui.theme.LocalCustomTypography
import kr.co.architecture.core.ui.noRippledClickable

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
  onSelectedTabChanced: (mainTabEnum: MainTabEnum) -> Unit = {},
  colors: CustomColors = LocalCustomColors.current,
  typography: CustomTypography = LocalCustomTypography.current,
  density: Density = LocalDensity.current
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(70.dp)
      .drawBehind {
        val thin = with(density) { 1.dp.toPx() }
        val y = size.height - thin / 2f
        drawLine(
          color = colors.border,
          start = Offset(0f, y),
          end   = Offset(size.width, y),
          strokeWidth = thin
        )
      }
      .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.Bottom
  ) {
    MainTabEnum.entries.forEachIndexed { index, mainTab ->
      val selected = mainTab == selectedTab

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
          text = stringResource(mainTab.tabStringRes),
          style = if (selected) typography.label else typography.labelMedium
        )

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .drawBehind {
              val thick = with(density) { 2.dp.toPx() }

              val startX = 0f
              val endX = size.width
              val y = size.height / 2f

              if (selected) {
                drawLine(
                  color = colors.divider,
                  start = Offset(startX, y),
                  end   = Offset(endX, y),
                  strokeWidth = thick,
                  cap = StrokeCap.Round
                )
              }
            }
        )
      }
    }
  }
}