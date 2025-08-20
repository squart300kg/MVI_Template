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

val IcEmojiDepressed: ImageVector
    get() {
        if (_icEmojiDepressed != null) {
            return _icEmojiDepressed!!
        }
        _icEmojiDepressed = Builder(name = "IcEmojiDepressed", defaultWidth = 50.0.dp, defaultHeight
                = 50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFF4C76C)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(25.0f, 50.0f)
                    curveTo(38.806f, 50.0f, 49.998f, 38.808f, 49.998f, 25.002f)
                    curveTo(49.998f, 11.196f, 38.806f, 0.004f, 25.0f, 0.004f)
                    curveTo(11.194f, 0.004f, 0.002f, 11.196f, 0.002f, 25.002f)
                    curveTo(0.002f, 38.808f, 11.194f, 50.0f, 25.0f, 50.0f)
                    close()
                }
                path(fill = linearGradient(0.099f to Color(0xFF5C6BEE), 0.516f to Color(0x2B2B50D7),
                        0.776f to Color(0x004768DD), start = Offset(25.0f,0.004f), end =
                        Offset(25.0f,51.499f)), stroke = null, strokeLineWidth = 0.0f, strokeLineCap
                        = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(25.0f, 50.0f)
                    curveTo(38.806f, 50.0f, 49.998f, 38.808f, 49.998f, 25.002f)
                    curveTo(49.998f, 11.196f, 38.806f, 0.004f, 25.0f, 0.004f)
                    curveTo(11.194f, 0.004f, 0.002f, 11.196f, 0.002f, 25.002f)
                    curveTo(0.002f, 38.808f, 11.194f, 50.0f, 25.0f, 50.0f)
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
                        pathFillType = NonZero) {
                    moveTo(17.044f, 30.649f)
                    curveTo(18.267f, 30.649f, 19.258f, 29.525f, 19.258f, 28.14f)
                    curveTo(19.258f, 26.754f, 18.267f, 25.631f, 17.044f, 25.631f)
                    curveTo(15.821f, 25.631f, 14.83f, 26.754f, 14.83f, 28.14f)
                    curveTo(14.83f, 29.525f, 15.821f, 30.649f, 17.044f, 30.649f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(35.17f, 28.129f)
                    curveTo(35.17f, 29.513f, 34.178f, 30.638f, 32.956f, 30.638f)
                    curveTo(31.734f, 30.638f, 30.742f, 29.516f, 30.742f, 28.129f)
                    curveTo(30.742f, 26.742f, 31.734f, 25.62f, 32.956f, 25.62f)
                    curveTo(34.178f, 25.62f, 35.17f, 26.742f, 35.17f, 28.129f)
                    close()
                }
            }
        }
        .build()
        return _icEmojiDepressed!!
    }

private var _icEmojiDepressed: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiDepressed, contentDescription = "")
    }
}
