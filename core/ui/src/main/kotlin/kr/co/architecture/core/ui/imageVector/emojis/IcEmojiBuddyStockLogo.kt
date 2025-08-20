package kr.co.architecture.core.ui.imageVector.emojis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val IcEmojiBuddyStockLogo: ImageVector
  get() {
    if (_buddystockavatar != null) {
      return _buddystockavatar!!
    }
    _buddystockavatar = Builder(
      name = "IcEmojiBuddyStockLogo", defaultWidth = 30.0.dp, defaultHeight
      = 30.0.dp, viewportWidth = 30.0f, viewportHeight = 30.0f
    ).apply {
      path(
        fill = SolidColor(Color(0xFFE3FCFC)), stroke = null, strokeLineWidth = 0.0f,
        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
        pathFillType = NonZero
      ) {
        moveTo(15.0f, 15.0f)
        moveToRelative(-15.0f, 0.0f)
        arcToRelative(15.0f, 15.0f, 0.0f, true, true, 30.0f, 0.0f)
        arcToRelative(15.0f, 15.0f, 0.0f, true, true, -30.0f, 0.0f)
      }
      path(
        fill = SolidColor(Color(0xFF00CCCF)), stroke = null, strokeLineWidth = 0.0f,
        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
        pathFillType = NonZero
      ) {
        moveTo(14.553f, 10.857f)
        lineTo(8.331f, 12.636f)
        lineTo(6.0f, 10.302f)
        verticalLineTo(20.314f)
        curveTo(6.0f, 20.546f, 6.054f, 20.775f, 6.157f, 20.983f)
        curveTo(6.261f, 21.191f, 6.411f, 21.372f, 6.596f, 21.511f)
        curveTo(6.781f, 21.651f, 6.997f, 21.746f, 7.225f, 21.788f)
        curveTo(7.453f, 21.831f, 7.687f, 21.82f, 7.911f, 21.756f)
        lineTo(15.404f, 19.613f)
        curveTo(15.717f, 19.524f, 15.993f, 19.334f, 16.189f, 19.074f)
        curveTo(16.385f, 18.814f, 16.491f, 18.497f, 16.491f, 18.171f)
        verticalLineTo(12.321f)
        curveTo(16.491f, 12.085f, 16.437f, 11.853f, 16.332f, 11.642f)
        curveTo(16.227f, 11.431f, 16.074f, 11.247f, 15.887f, 11.105f)
        curveTo(15.699f, 10.963f, 15.48f, 10.867f, 15.249f, 10.824f)
        curveTo(15.017f, 10.781f, 14.779f, 10.792f, 14.553f, 10.857f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF3333FF)), stroke = null, strokeLineWidth = 0.0f,
        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
        pathFillType = NonZero
      ) {
        moveTo(20.987f, 11.159f)
        lineTo(14.58f, 12.991f)
        curveTo(14.267f, 13.081f, 13.992f, 13.27f, 13.796f, 13.531f)
        curveTo(13.6f, 13.791f, 13.493f, 14.108f, 13.493f, 14.434f)
        verticalLineTo(20.314f)
        curveTo(13.493f, 20.546f, 13.547f, 20.775f, 13.651f, 20.983f)
        curveTo(13.754f, 21.191f, 13.905f, 21.372f, 14.09f, 21.512f)
        curveTo(14.275f, 21.651f, 14.49f, 21.746f, 14.718f, 21.789f)
        curveTo(14.946f, 21.831f, 15.181f, 21.82f, 15.404f, 21.756f)
        lineTo(22.898f, 19.613f)
        curveTo(23.211f, 19.524f, 23.486f, 19.335f, 23.683f, 19.074f)
        curveTo(23.879f, 18.814f, 23.985f, 18.497f, 23.985f, 18.171f)
        verticalLineTo(8.16f)
        lineTo(20.987f, 11.159f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF2424B2)), stroke = null, fillAlpha = 0.9f, strokeAlpha
        = 0.9f, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
        strokeLineMiter = 4.0f, pathFillType = NonZero
      ) {
        moveTo(15.404f, 19.613f)
        curveTo(15.717f, 19.524f, 15.993f, 19.334f, 16.189f, 19.074f)
        curveTo(16.385f, 18.814f, 16.491f, 18.497f, 16.491f, 18.171f)
        verticalLineTo(12.445f)
        lineTo(14.58f, 12.991f)
        curveTo(14.267f, 13.081f, 13.992f, 13.27f, 13.796f, 13.53f)
        curveTo(13.6f, 13.791f, 13.494f, 14.108f, 13.493f, 14.434f)
        verticalLineTo(20.159f)
        lineTo(15.404f, 19.613f)
        close()
      }
    }
      .build()
    return _buddystockavatar!!
  }

private var _buddystockavatar: ImageVector? = null

@Preview
@Composable
private fun Preview() {
  Box(modifier = Modifier.padding(12.dp)) {
    Image(imageVector = IcEmojiBuddyStockLogo, contentDescription = "")
  }
}
