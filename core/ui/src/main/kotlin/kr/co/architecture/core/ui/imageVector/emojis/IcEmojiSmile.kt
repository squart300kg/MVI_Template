package kr.co.architecture.core.ui.imageVector.emojis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
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

val IcEmojiSmile: ImageVector
    get() {
        if (_icEmojiSmile != null) {
            return _icEmojiSmile!!
        }
        _icEmojiSmile = Builder(name = "IcEmojiSmile", defaultWidth = 50.0.dp, defaultHeight =
                50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFA3DB6C)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(24.998f, 49.996f)
                    curveTo(38.804f, 49.996f, 49.996f, 38.804f, 49.996f, 24.998f)
                    curveTo(49.996f, 11.192f, 38.804f, 0.0f, 24.998f, 0.0f)
                    curveTo(11.192f, 0.0f, -0.0f, 11.192f, -0.0f, 24.998f)
                    curveTo(-0.0f, 38.804f, 11.192f, 49.996f, 24.998f, 49.996f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(24.567f, 28.14f)
                    curveTo(24.567f, 29.524f, 23.575f, 30.649f, 22.353f, 30.649f)
                    curveTo(21.131f, 30.649f, 20.139f, 29.527f, 20.139f, 28.14f)
                    curveTo(20.139f, 26.752f, 21.131f, 25.631f, 22.353f, 25.631f)
                    curveTo(23.575f, 25.631f, 24.567f, 26.752f, 24.567f, 28.14f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(38.265f, 30.638f)
                    curveTo(39.487f, 30.638f, 40.479f, 29.514f, 40.479f, 28.129f)
                    curveTo(40.479f, 26.743f, 39.487f, 25.62f, 38.265f, 25.62f)
                    curveTo(37.042f, 25.62f, 36.051f, 26.743f, 36.051f, 28.129f)
                    curveTo(36.051f, 29.514f, 37.042f, 30.638f, 38.265f, 30.638f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd) {
                    moveTo(34.389f, 31.587f)
                    curveTo(34.855f, 31.757f, 35.095f, 32.273f, 34.925f, 32.739f)
                    curveTo(34.242f, 34.607f, 32.487f, 35.523f, 30.795f, 35.536f)
                    curveTo(29.098f, 35.549f, 27.314f, 34.657f, 26.536f, 32.774f)
                    curveTo(26.347f, 32.316f, 26.565f, 31.79f, 27.024f, 31.601f)
                    curveTo(27.482f, 31.411f, 28.008f, 31.629f, 28.197f, 32.088f)
                    curveTo(28.652f, 33.188f, 29.696f, 33.747f, 30.782f, 33.739f)
                    curveTo(31.874f, 33.731f, 32.859f, 33.155f, 33.237f, 32.123f)
                    curveTo(33.407f, 31.656f, 33.923f, 31.417f, 34.389f, 31.587f)
                    close()
                }
            }
        }
        .build()
        return _icEmojiSmile!!
    }

private var _icEmojiSmile: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiSmile, contentDescription = "")
    }
}
