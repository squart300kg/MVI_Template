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

val IcEmojiWater: ImageVector
    get() {
        if (_icEmojiWater != null) {
            return _icEmojiWater!!
        }
        _icEmojiWater = Builder(name = "IcEmojiWater", defaultWidth = 50.0.dp, defaultHeight =
                50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            path(fill = SolidColor(Color(0xFFB9F7F6)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(24.998f, 49.996f)
                curveTo(38.804f, 49.996f, 49.996f, 38.804f, 49.996f, 24.998f)
                curveTo(49.996f, 11.192f, 38.804f, 0.0f, 24.998f, 0.0f)
                curveTo(11.192f, 0.0f, 0.0f, 11.192f, 0.0f, 24.998f)
                curveTo(0.0f, 38.804f, 11.192f, 49.996f, 24.998f, 49.996f)
                close()
            }
            path(fill = SolidColor(Color(0xFF38BBF7)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(25.366f, 8.0f)
                curveTo(12.806f, 21.828f, 14.033f, 29.898f, 14.033f, 29.898f)
                curveTo(13.933f, 35.925f, 18.747f, 40.89f, 24.799f, 40.999f)
                curveTo(30.848f, 41.098f, 35.831f, 36.301f, 35.94f, 30.273f)
                curveTo(35.94f, 30.273f, 37.445f, 22.244f, 25.366f, 8.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6FDCFF)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(20.936f, 27.561f)
                curveTo(20.441f, 29.714f, 18.949f, 31.208f, 17.603f, 30.899f)
                curveTo(16.258f, 30.589f, 15.568f, 28.593f, 16.064f, 26.44f)
                curveTo(16.559f, 24.287f, 18.051f, 22.793f, 19.397f, 23.102f)
                curveTo(20.742f, 23.412f, 21.432f, 25.408f, 20.936f, 27.561f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6FDCFF)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(21.827f, 20.174f)
                curveTo(21.622f, 21.063f, 20.907f, 21.657f, 20.228f, 21.501f)
                curveTo(19.55f, 21.345f, 19.166f, 20.498f, 19.37f, 19.609f)
                curveTo(19.574f, 18.721f, 20.29f, 18.127f, 20.969f, 18.283f)
                curveTo(21.647f, 18.439f, 22.031f, 19.286f, 21.827f, 20.174f)
                close()
            }
        }
        .build()
        return _icEmojiWater!!
    }

private var _icEmojiWater: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiWater, contentDescription = "")
    }
}
