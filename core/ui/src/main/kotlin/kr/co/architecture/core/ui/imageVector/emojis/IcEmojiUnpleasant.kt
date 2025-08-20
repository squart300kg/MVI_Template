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

val IcEmojiUnpleasant: ImageVector
    get() {
        if (_icEmojiUnpleasant != null) {
            return _icEmojiUnpleasant!!
        }
        _icEmojiUnpleasant = Builder(name = "IcEmojiUnpleasant", defaultWidth = 50.0.dp,
                defaultHeight = 50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFFC98BC)), stroke = null, strokeLineWidth = 0.0f,
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
                    moveTo(24.567f, 20.951f)
                    curveTo(24.567f, 22.335f, 23.575f, 23.46f, 22.353f, 23.46f)
                    curveTo(21.131f, 23.46f, 20.139f, 22.339f, 20.139f, 20.951f)
                    curveTo(20.139f, 19.564f, 21.131f, 18.442f, 22.353f, 18.442f)
                    curveTo(23.575f, 18.442f, 24.567f, 19.564f, 24.567f, 20.951f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(38.265f, 23.449f)
                    curveTo(39.487f, 23.449f, 40.479f, 22.326f, 40.479f, 20.94f)
                    curveTo(40.479f, 19.555f, 39.487f, 18.432f, 38.265f, 18.432f)
                    curveTo(37.042f, 18.432f, 36.051f, 19.555f, 36.051f, 20.94f)
                    curveTo(36.051f, 22.326f, 37.042f, 23.449f, 38.265f, 23.449f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd) {
                    moveTo(30.795f, 24.345f)
                    curveTo(32.487f, 24.357f, 34.242f, 25.274f, 34.925f, 27.141f)
                    curveTo(35.095f, 27.607f, 34.855f, 28.123f, 34.389f, 28.294f)
                    curveTo(33.923f, 28.464f, 33.407f, 28.224f, 33.237f, 27.758f)
                    curveTo(32.859f, 26.725f, 31.874f, 26.15f, 30.782f, 26.142f)
                    curveTo(29.696f, 26.134f, 28.652f, 26.692f, 28.197f, 27.793f)
                    curveTo(28.008f, 28.252f, 27.482f, 28.47f, 27.024f, 28.28f)
                    curveTo(26.565f, 28.091f, 26.347f, 27.565f, 26.536f, 27.107f)
                    curveTo(27.314f, 25.224f, 29.098f, 24.332f, 30.795f, 24.345f)
                    close()
                }
            }
        }
        .build()
        return _icEmojiUnpleasant!!
    }

private var _icEmojiUnpleasant: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiUnpleasant, contentDescription = "")
    }
}
