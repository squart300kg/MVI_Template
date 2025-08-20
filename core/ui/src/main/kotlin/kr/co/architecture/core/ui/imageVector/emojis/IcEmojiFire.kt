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

val IcEmojiFire: ImageVector
    get() {
        if (_icEmojiFire != null) {
            return _icEmojiFire!!
        }
        _icEmojiFire = Builder(name = "IcEmojiFire", defaultWidth = 50.0.dp, defaultHeight =
                50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            path(fill = SolidColor(Color(0xFFFFA991)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(24.998f, 49.996f)
                curveTo(38.804f, 49.996f, 49.996f, 38.804f, 49.996f, 24.998f)
                curveTo(49.996f, 11.192f, 38.804f, 0.0f, 24.998f, 0.0f)
                curveTo(11.192f, 0.0f, 0.0f, 11.192f, 0.0f, 24.998f)
                curveTo(0.0f, 38.804f, 11.192f, 49.996f, 24.998f, 49.996f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE2544B)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(12.427f, 18.169f)
                curveTo(7.722f, 20.451f, 7.722f, 41.391f, 25.603f, 41.542f)
                curveTo(31.649f, 41.592f, 38.539f, 37.79f, 39.244f, 31.737f)
                curveTo(39.948f, 25.684f, 36.997f, 22.64f, 36.997f, 22.64f)
                curveTo(36.997f, 22.64f, 35.247f, 27.345f, 32.544f, 27.697f)
                curveTo(32.544f, 27.697f, 34.844f, 21.565f, 29.787f, 16.271f)
                curveTo(24.73f, 10.976f, 26.132f, 6.451f, 26.132f, 6.451f)
                curveTo(26.132f, 6.451f, 14.95f, 12.543f, 17.84f, 23.812f)
                curveTo(17.84f, 23.812f, 14.429f, 22.349f, 12.427f, 18.165f)
                verticalLineTo(18.169f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF4C76C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(31.61f, 32.725f)
                curveTo(31.89f, 31.654f, 31.94f, 30.604f, 31.771f, 29.767f)
                curveTo(30.186f, 22.021f, 23.393f, 24.242f, 24.798f, 13.388f)
                curveTo(24.798f, 13.388f, 18.124f, 20.508f, 20.798f, 31.337f)
                curveTo(19.001f, 31.532f, 15.112f, 29.08f, 14.195f, 27.632f)
                curveTo(14.195f, 27.632f, 14.842f, 38.041f, 25.413f, 38.041f)
                curveTo(25.575f, 38.041f, 25.733f, 38.034f, 25.887f, 38.023f)
                curveTo(25.887f, 38.023f, 28.924f, 37.879f, 30.715f, 36.999f)
                curveTo(33.773f, 35.493f, 34.23f, 30.453f, 34.23f, 30.453f)
                curveTo(34.23f, 30.453f, 33.166f, 31.877f, 31.606f, 32.725f)
                horizontalLineTo(31.61f)
                close()
            }
        }
        .build()
        return _icEmojiFire!!
    }

private var _icEmojiFire: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiFire, contentDescription = "")
    }
}
