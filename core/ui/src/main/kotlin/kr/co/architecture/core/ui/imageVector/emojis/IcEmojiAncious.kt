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

val IcEmojiAncious: ImageVector
    get() {
        if (_icEmojiAncious != null) {
            return _icEmojiAncious!!
        }
        _icEmojiAncious = Builder(name = "IcEmojiAncious", defaultWidth = 50.0.dp, defaultHeight =
                50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFFC8863)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(25.0f, 50.001f)
                    curveTo(38.806f, 50.001f, 49.998f, 38.809f, 49.998f, 25.003f)
                    curveTo(49.998f, 11.197f, 38.806f, 0.005f, 25.0f, 0.005f)
                    curveTo(11.194f, 0.005f, 0.002f, 11.197f, 0.002f, 25.003f)
                    curveTo(0.002f, 38.809f, 11.194f, 50.001f, 25.0f, 50.001f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(20.987f, 36.687f)
                    curveTo(22.177f, 36.536f, 23.535f, 36.432f, 25.034f, 36.432f)
                    curveTo(26.504f, 36.432f, 27.838f, 36.536f, 29.009f, 36.687f)
                    curveTo(29.304f, 36.687f, 29.541f, 36.45f, 29.541f, 36.155f)
                    curveTo(29.541f, 33.646f, 27.507f, 31.608f, 24.995f, 31.608f)
                    curveTo(22.482f, 31.608f, 20.448f, 33.643f, 20.448f, 36.155f)
                    curveTo(20.448f, 36.45f, 20.685f, 36.687f, 20.98f, 36.687f)
                    horizontalLineTo(20.987f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd) {
                    moveTo(16.361f, 25.758f)
                    curveTo(16.01f, 25.407f, 15.441f, 25.406f, 15.09f, 25.757f)
                    curveTo(14.739f, 26.108f, 14.738f, 26.677f, 15.089f, 27.028f)
                    lineTo(15.857f, 27.796f)
                    lineTo(15.089f, 28.565f)
                    curveTo(14.738f, 28.916f, 14.739f, 29.485f, 15.09f, 29.836f)
                    curveTo(15.441f, 30.186f, 16.01f, 30.186f, 16.361f, 29.835f)
                    lineTo(17.127f, 29.068f)
                    lineTo(17.893f, 29.835f)
                    curveTo(18.243f, 30.186f, 18.812f, 30.186f, 19.163f, 29.836f)
                    curveTo(19.515f, 29.485f, 19.515f, 28.916f, 19.164f, 28.565f)
                    lineTo(18.397f, 27.796f)
                    lineTo(19.164f, 27.028f)
                    curveTo(19.515f, 26.677f, 19.515f, 26.108f, 19.163f, 25.757f)
                    curveTo(18.812f, 25.406f, 18.243f, 25.407f, 17.893f, 25.758f)
                    lineTo(17.127f, 26.525f)
                    lineTo(16.361f, 25.758f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd) {
                    moveTo(32.279f, 25.775f)
                    curveTo(31.928f, 25.424f, 31.36f, 25.424f, 31.009f, 25.775f)
                    curveTo(30.658f, 26.126f, 30.658f, 26.695f, 31.009f, 27.046f)
                    lineTo(31.775f, 27.812f)
                    lineTo(31.009f, 28.578f)
                    curveTo(30.658f, 28.929f, 30.658f, 29.498f, 31.009f, 29.849f)
                    curveTo(31.36f, 30.2f, 31.928f, 30.2f, 32.279f, 29.849f)
                    lineTo(33.046f, 29.083f)
                    lineTo(33.812f, 29.849f)
                    curveTo(34.163f, 30.2f, 34.732f, 30.2f, 35.083f, 29.849f)
                    curveTo(35.434f, 29.498f, 35.434f, 28.929f, 35.083f, 28.578f)
                    lineTo(34.317f, 27.812f)
                    lineTo(35.083f, 27.046f)
                    curveTo(35.434f, 26.695f, 35.434f, 26.126f, 35.083f, 25.775f)
                    curveTo(34.732f, 25.424f, 34.163f, 25.424f, 33.812f, 25.775f)
                    lineTo(33.046f, 26.541f)
                    lineTo(32.279f, 25.775f)
                    close()
                }
            }
        }
        .build()
        return _icEmojiAncious!!
    }

private var _icEmojiAncious: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiAncious, contentDescription = "")
    }
}
