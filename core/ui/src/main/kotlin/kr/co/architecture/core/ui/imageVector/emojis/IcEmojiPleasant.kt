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
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val IcEmojiPleasant: ImageVector
    get() {
        if (_icEmojiPleasant != null) {
            return _icEmojiPleasant!!
        }
        _icEmojiPleasant = Builder(name = "IcEmojiPleasant", defaultWidth = 50.0.dp, defaultHeight =
                50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFF6BCDFA)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(25.0f, 25.0f)
                    moveToRelative(-25.0f, 0.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, true, true, 50.0f, 0.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, true, true, -50.0f, 0.0f)
                }
                path(fill = SolidColor(Color(0xFF091E42)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(29.013f, 31.608f)
                    curveTo(27.824f, 31.759f, 26.465f, 31.863f, 24.966f, 31.863f)
                    curveTo(23.496f, 31.863f, 22.163f, 31.759f, 20.991f, 31.608f)
                    curveTo(20.696f, 31.608f, 20.459f, 31.845f, 20.459f, 32.14f)
                    curveTo(20.459f, 34.649f, 22.493f, 36.687f, 25.006f, 36.687f)
                    curveTo(27.518f, 36.687f, 29.552f, 34.652f, 29.552f, 32.14f)
                    curveTo(29.552f, 31.845f, 29.315f, 31.608f, 29.021f, 31.608f)
                    horizontalLineTo(29.013f)
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
                    moveTo(32.956f, 30.638f)
                    curveTo(34.179f, 30.638f, 35.17f, 29.514f, 35.17f, 28.129f)
                    curveTo(35.17f, 26.743f, 34.179f, 25.62f, 32.956f, 25.62f)
                    curveTo(31.733f, 25.62f, 30.742f, 26.743f, 30.742f, 28.129f)
                    curveTo(30.742f, 29.514f, 31.733f, 30.638f, 32.956f, 30.638f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFD38B6C)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(27.402f, 35.247f)
                    curveTo(27.51f, 35.17f, 27.51f, 35.021f, 27.402f, 34.944f)
                    curveTo(26.791f, 34.493f, 26.008f, 34.2f, 24.998f, 34.2f)
                    curveTo(23.988f, 34.2f, 23.204f, 34.493f, 22.593f, 34.944f)
                    curveTo(22.485f, 35.021f, 22.489f, 35.17f, 22.593f, 35.247f)
                    curveTo(23.244f, 35.716f, 24.081f, 36.0f, 24.994f, 36.0f)
                    curveTo(25.907f, 36.0f, 26.748f, 35.716f, 27.395f, 35.247f)
                    horizontalLineTo(27.402f)
                    close()
                }
            }
        }
        .build()
        return _icEmojiPleasant!!
    }

private var _icEmojiPleasant: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiPleasant, contentDescription = "")
    }
}
