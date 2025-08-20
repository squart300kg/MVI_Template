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

val IcEmojiPoo: ImageVector
    get() {
        if (_icEmojiPoo != null) {
            return _icEmojiPoo!!
        }
        _icEmojiPoo = Builder(name = "IcEmojiPoo", defaultWidth = 50.0.dp, defaultHeight = 50.0.dp,
                viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            path(fill = SolidColor(Color(0xFFF4C76C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(25.0f, 50.0f)
                curveTo(38.807f, 50.0f, 50.0f, 38.807f, 50.0f, 25.0f)
                curveTo(50.0f, 11.193f, 38.807f, 0.0f, 25.0f, 0.0f)
                curveTo(11.193f, 0.0f, 0.0f, 11.193f, 0.0f, 25.0f)
                curveTo(0.0f, 38.807f, 11.193f, 50.0f, 25.0f, 50.0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF895F3B)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(26.702f, 6.454f)
                horizontalLineTo(24.074f)
                curveTo(23.894f, 6.454f, 23.718f, 6.461f, 23.546f, 6.476f)
                curveTo(24.804f, 6.623f, 25.778f, 7.626f, 25.778f, 8.844f)
                curveTo(25.778f, 10.164f, 24.638f, 11.235f, 23.233f, 11.235f)
                horizontalLineTo(19.549f)
                curveTo(17.28f, 11.235f, 15.44f, 12.96f, 15.44f, 15.088f)
                curveTo(15.44f, 17.216f, 17.28f, 18.941f, 19.549f, 18.941f)
                horizontalLineTo(20.249f)
                curveTo(21.36f, 19.596f, 22.672f, 19.977f, 24.078f, 19.977f)
                horizontalLineTo(26.705f)
                curveTo(30.684f, 19.977f, 33.912f, 16.95f, 33.912f, 13.215f)
                curveTo(33.912f, 9.481f, 30.688f, 6.454f, 26.705f, 6.454f)
                horizontalLineTo(26.702f)
                close()
            }
            path(fill = SolidColor(Color(0xFF895F3B)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(33.618f, 16.749f)
                horizontalLineTo(16.317f)
                curveTo(12.98f, 16.749f, 10.275f, 19.454f, 10.275f, 22.79f)
                curveTo(10.275f, 26.126f, 12.98f, 28.83f, 16.317f, 28.83f)
                horizontalLineTo(33.618f)
                curveTo(36.955f, 28.83f, 39.66f, 26.126f, 39.66f, 22.79f)
                curveTo(39.66f, 19.454f, 36.955f, 16.749f, 33.618f, 16.749f)
                close()
            }
            path(fill = SolidColor(Color(0xFF895F3B)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(37.198f, 39.761f)
                horizontalLineTo(12.784f)
                curveTo(9.052f, 39.761f, 6.026f, 36.734f, 6.026f, 33.003f)
                curveTo(6.026f, 29.272f, 9.052f, 26.245f, 12.784f, 26.245f)
                horizontalLineTo(37.198f)
                curveTo(40.929f, 26.245f, 43.955f, 29.272f, 43.955f, 33.003f)
                curveTo(43.955f, 36.734f, 40.929f, 39.761f, 37.198f, 39.761f)
                close()
            }
            path(fill = SolidColor(Color(0xFF7A502C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(37.119f, 35.599f)
                horizontalLineTo(12.859f)
                curveTo(9.868f, 35.599f, 7.334f, 33.64f, 6.468f, 30.937f)
                curveTo(6.26f, 31.583f, 6.145f, 32.274f, 6.145f, 32.989f)
                curveTo(6.145f, 36.699f, 9.15f, 39.704f, 12.859f, 39.704f)
                horizontalLineTo(37.119f)
                curveTo(40.828f, 39.704f, 43.833f, 36.699f, 43.833f, 32.989f)
                curveTo(43.833f, 32.274f, 43.718f, 31.583f, 43.51f, 30.937f)
                curveTo(42.644f, 33.64f, 40.109f, 35.599f, 37.119f, 35.599f)
                close()
            }
            path(fill = SolidColor(Color(0xFF7A502C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(28.136f, 8.841f)
                curveTo(28.136f, 7.622f, 27.162f, 6.619f, 25.904f, 6.472f)
                curveTo(26.08f, 6.461f, 26.256f, 6.45f, 26.432f, 6.45f)
                horizontalLineTo(24.07f)
                curveTo(23.891f, 6.45f, 23.715f, 6.457f, 23.542f, 6.472f)
                curveTo(24.8f, 6.619f, 25.774f, 7.622f, 25.774f, 8.841f)
                curveTo(25.774f, 10.16f, 24.635f, 11.231f, 23.229f, 11.231f)
                horizontalLineTo(25.591f)
                curveTo(26.996f, 11.231f, 28.136f, 10.16f, 28.136f, 8.841f)
                close()
            }
            path(fill = SolidColor(Color(0xFF7A502C)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(18.204f, 16.748f)
                curveTo(17.945f, 16.245f, 17.798f, 15.681f, 17.798f, 15.084f)
                curveTo(17.798f, 12.956f, 19.638f, 11.231f, 21.907f, 11.231f)
                horizontalLineTo(19.545f)
                curveTo(17.277f, 11.231f, 15.436f, 12.956f, 15.436f, 15.084f)
                curveTo(15.436f, 15.688f, 15.591f, 16.259f, 15.853f, 16.77f)
                curveTo(16.004f, 16.759f, 16.159f, 16.748f, 16.313f, 16.748f)
                horizontalLineTo(18.204f)
                close()
            }
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(17.891f, 31.626f)
                curveTo(20.482f, 31.626f, 22.582f, 28.832f, 22.582f, 25.386f)
                curveTo(22.582f, 21.939f, 20.482f, 19.146f, 17.891f, 19.146f)
                curveTo(15.301f, 19.146f, 13.201f, 21.939f, 13.201f, 25.386f)
                curveTo(13.201f, 28.832f, 15.301f, 31.626f, 17.891f, 31.626f)
                close()
            }
            path(fill = SolidColor(Color(0xFF2E293A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(17.891f, 28.251f)
                curveTo(19.357f, 28.251f, 20.544f, 27.063f, 20.544f, 25.598f)
                curveTo(20.544f, 24.133f, 19.357f, 22.945f, 17.891f, 22.945f)
                curveTo(16.426f, 22.945f, 15.239f, 24.133f, 15.239f, 25.598f)
                curveTo(15.239f, 27.063f, 16.426f, 28.251f, 17.891f, 28.251f)
                close()
            }
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(32.043f, 31.626f)
                curveTo(34.634f, 31.626f, 36.734f, 28.832f, 36.734f, 25.386f)
                curveTo(36.734f, 21.939f, 34.634f, 19.146f, 32.043f, 19.146f)
                curveTo(29.452f, 19.146f, 27.352f, 21.939f, 27.352f, 25.386f)
                curveTo(27.352f, 28.832f, 29.452f, 31.626f, 32.043f, 31.626f)
                close()
            }
            path(fill = SolidColor(Color(0xFF2E293A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(32.043f, 28.251f)
                curveTo(33.508f, 28.251f, 34.696f, 27.063f, 34.696f, 25.598f)
                curveTo(34.696f, 24.133f, 33.508f, 22.945f, 32.043f, 22.945f)
                curveTo(30.578f, 22.945f, 29.39f, 24.133f, 29.39f, 25.598f)
                curveTo(29.39f, 27.063f, 30.578f, 28.251f, 32.043f, 28.251f)
                close()
            }
            path(fill = SolidColor(Color(0xFF2E293A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(28.977f, 32.215f)
                curveTo(27.787f, 32.366f, 26.428f, 32.47f, 24.93f, 32.47f)
                curveTo(23.459f, 32.47f, 22.126f, 32.366f, 20.954f, 32.215f)
                curveTo(20.659f, 32.215f, 20.422f, 32.452f, 20.422f, 32.747f)
                curveTo(20.422f, 35.256f, 22.456f, 37.294f, 24.969f, 37.294f)
                curveTo(27.482f, 37.294f, 29.516f, 35.259f, 29.516f, 32.747f)
                curveTo(29.516f, 32.452f, 29.279f, 32.215f, 28.984f, 32.215f)
                horizontalLineTo(28.977f)
                close()
            }
        }
        .build()
        return _icEmojiPoo!!
    }

private var _icEmojiPoo: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiPoo, contentDescription = "")
    }
}
