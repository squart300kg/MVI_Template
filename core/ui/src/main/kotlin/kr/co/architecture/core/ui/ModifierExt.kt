package kr.co.architecture.core.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Objects
import kotlin.math.min
import kotlin.to


fun Modifier.baseClickable(
  delayMillis: Long = 1000L,
  onClick: () -> Unit
): Modifier = composed {
  var isClickable by remember { mutableStateOf(true) }

  LaunchedEffect(isClickable) {
    if (!isClickable) {
      delay(delayMillis)
      isClickable = true
    }
  }

  this.clickable {
    if (isClickable) {
      isClickable = false
      onClick()
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.noRippledClickable(
  onClick: () -> Unit = { },
  onLongClick: () -> Unit = { },
  delayMillis: Long = 1000L
): Modifier = composed {
  var isClickable by remember { mutableStateOf(true) }

  fun clickBuilder(builder: () -> Unit) {
    if (isClickable) {
      isClickable = false
      builder()
    }
  }
  LaunchedEffect(isClickable) {
    if (!isClickable) {
      delay(delayMillis)
      isClickable = true
    }
  }
  this.combinedClickable(
    onClick = { clickBuilder(onClick) },
    onLongClick = { clickBuilder(onLongClick) },
    indication = null,
    interactionSource = remember { MutableInteractionSource() }
  )
}

@Composable
fun Modifier.extractComposableHeight(onMeasuredHeight: (heightInPx: Int) -> Unit) =
  onGloballyPositioned { onMeasuredHeight(it.size.height) }

fun Modifier.heightWithNoComposition(heightProvider: () -> Float): Modifier =
  this.layout { measurable, _ ->
    val height = heightProvider()
      .toInt()

    val constraints = Constraints.fixedHeight(
      height = height
    )

    val placeable = measurable.measure(constraints)
    layout(placeable.width, placeable.height) {
      placeable.place(0, 0)
    }
  }

@Stable
fun Modifier.recomposeHighlighter(): Modifier = this.then(RecomposeHighlighterElement())

private class RecomposeHighlighterElement : ModifierNodeElement<RecomposeHighlighterModifier>() {

  override fun InspectorInfo.inspectableProperties() {
    debugInspectorInfo { name = "recomposeHighlighter" }
  }

  override fun create(): RecomposeHighlighterModifier = RecomposeHighlighterModifier()

  override fun update(node: RecomposeHighlighterModifier) {
    node.incrementCompositions()
  }

  // It's never equal, so that every recomposition triggers the update function.
  override fun equals(other: Any?): Boolean = false

  override fun hashCode(): Int = Objects.hash(this)
}

private class RecomposeHighlighterModifier : Modifier.Node(), DrawModifierNode {

  private var timerJob: Job? = null

  /**
   * The total number of compositions that have occurred.
   */
  private var totalCompositions: Long = 0
    set(value) {
      if (field == value) return
      restartTimer()
      field = value
      invalidateDraw()
    }

  fun incrementCompositions() {
    totalCompositions++
  }

  override fun onAttach() {
    super.onAttach()
    restartTimer()
  }

  override val shouldAutoInvalidate: Boolean = false

  override fun onDetach() {
    timerJob?.cancel()
  }

  /**
   * Start the timeout, and reset everytime there's a recomposition.
   */
  private fun restartTimer() {
    if (!isAttached) return

    timerJob?.cancel()
    timerJob = coroutineScope.launch {
      delay(3000)
      totalCompositions = 0
      invalidateDraw()
    }
  }

  override fun ContentDrawScope.draw() {
    // Draw actual content.
    drawContent()

    // Below is to draw the highlight, if necessary. A lot of the logic is copied from Modifier.border

    val hasValidBorderParams = size.minDimension > 0f
    if (!hasValidBorderParams || totalCompositions <= 0) {
      return
    }

    val (color, strokeWidthPx) =
      when (totalCompositions) {
        // We need at least one composition to draw, so draw the smallest border
        // color in blue.
        1L -> Color.Blue to 1f
        // 2 compositions is _probably_ okay.
        2L -> Color.Green to 2.dp.toPx()
        // 3 or more compositions before timeout may indicate an issue. lerp the
        // color from yellow to red, and continually increase the border size.
        else -> {
          lerp(
            Color.Yellow.copy(alpha = 0.8f),
            Color.Red.copy(alpha = 0.5f),
            min(1f, (totalCompositions - 1).toFloat() / 100f)
          ) to totalCompositions.toInt().dp.toPx()
        }
      }

    val halfStroke = strokeWidthPx / 2
    val topLeft = Offset(halfStroke, halfStroke)
    val borderSize = Size(size.width - strokeWidthPx, size.height - strokeWidthPx)

    val fillArea = (strokeWidthPx * 2) > size.minDimension
    val rectTopLeft = if (fillArea) Offset.Zero else topLeft
    val size = if (fillArea) size else borderSize
    val style = if (fillArea) Fill else Stroke(strokeWidthPx)

    drawRect(
      brush = SolidColor(color),
      topLeft = rectTopLeft,
      size = size,
      style = style
    )
  }
}
