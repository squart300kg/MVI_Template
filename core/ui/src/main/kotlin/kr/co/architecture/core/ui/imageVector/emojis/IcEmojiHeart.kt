package kr.co.architecture.core.ui.imageVector.emojis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val IcEmojiHeart: ImageVector
    get() {
        if (_icEmojiHeart != null) {
            return _icEmojiHeart!!
        }
        _icEmojiHeart = Builder(name = "IcEmojiHeart", defaultWidth = 50.0.dp, defaultHeight =
                50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            path(fill = SolidColor(Color(0xFFFFC1B4)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(25.0f, 50.0f)
                curveTo(38.807f, 50.0f, 50.0f, 38.807f, 50.0f, 25.0f)
                curveTo(50.0f, 11.193f, 38.807f, 0.0f, 25.0f, 0.0f)
                curveTo(11.193f, 0.0f, 0.0f, 11.193f, 0.0f, 25.0f)
                curveTo(0.0f, 38.807f, 11.193f, 50.0f, 25.0f, 50.0f)
                close()
            }
            group {
                path(fill = linearGradient(0.0f to Color(0xFFF96E92), 1.0f to Color(0xFFDD4B78),
                        start = Offset(10.303f,16.151f), end = Offset(37.974f,31.826f)), stroke =
                        null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero) {
                    moveTo(41.0f, 22.101f)
                    curveTo(41.0f, 17.244f, 37.58f, 12.0f, 32.057f, 12.0f)
                    curveTo(28.512f, 12.0f, 26.248f, 13.755f, 25.002f, 15.156f)
                    curveTo(23.755f, 13.755f, 21.488f, 12.0f, 17.943f, 12.0f)
                    curveTo(12.42f, 12.0f, 9.0f, 17.244f, 9.0f, 22.101f)
                    curveTo(9.0f, 28.435f, 14.797f, 33.434f, 23.132f, 39.614f)
                    lineTo(23.188f, 39.654f)
                    curveTo(23.421f, 39.826f, 23.654f, 39.998f, 23.887f, 40.17f)
                    lineTo(25.006f, 40.996f)
                    lineTo(26.17f, 40.133f)
                    curveTo(26.354f, 39.998f, 26.534f, 39.866f, 26.714f, 39.731f)
                    lineTo(26.767f, 39.694f)
                    curveTo(35.154f, 33.474f, 41.007f, 28.456f, 41.007f, 22.097f)
                    lineTo(41.0f, 22.101f)
                    close()
                }
            }
        }
        .build()
        return _icEmojiHeart!!
    }

private var _icEmojiHeart: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiHeart, contentDescription = "")
    }
}
