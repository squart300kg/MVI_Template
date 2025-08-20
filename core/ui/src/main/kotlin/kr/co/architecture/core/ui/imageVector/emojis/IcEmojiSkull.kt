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

val IcEmojiSkull: ImageVector
    get() {
        if (_icEmojiSkull != null) {
            return _icEmojiSkull!!
        }
        _icEmojiSkull = Builder(name = "IcEmojiSkull", defaultWidth = 50.0.dp, defaultHeight =
                50.0.dp, viewportWidth = 50.0f, viewportHeight = 50.0f).apply {
            path(fill = SolidColor(Color(0xFF97A0AF)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(25.002f, 49.996f)
                curveTo(38.808f, 49.996f, 50.0f, 38.804f, 50.0f, 24.998f)
                curveTo(50.0f, 11.192f, 38.808f, 0.0f, 25.002f, 0.0f)
                curveTo(11.196f, 0.0f, 0.004f, 11.192f, 0.004f, 24.998f)
                curveTo(0.004f, 38.804f, 11.196f, 49.996f, 25.002f, 49.996f)
                close()
            }
            group {
                path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(39.486f, 20.303f)
                    curveTo(39.484f, 20.242f, 39.48f, 20.179f, 39.476f, 20.117f)
                    curveTo(39.465f, 19.944f, 39.452f, 19.773f, 39.436f, 19.604f)
                    curveTo(39.432f, 19.554f, 39.428f, 19.504f, 39.423f, 19.453f)
                    curveTo(39.401f, 19.25f, 39.375f, 19.048f, 39.345f, 18.851f)
                    curveTo(39.334f, 18.783f, 39.323f, 18.717f, 39.312f, 18.651f)
                    curveTo(39.288f, 18.513f, 39.264f, 18.375f, 39.236f, 18.239f)
                    curveTo(39.22f, 18.163f, 39.205f, 18.086f, 39.188f, 18.011f)
                    curveTo(39.151f, 17.847f, 39.111f, 17.683f, 39.07f, 17.523f)
                    curveTo(39.046f, 17.429f, 39.018f, 17.334f, 38.991f, 17.24f)
                    curveTo(38.961f, 17.135f, 38.93f, 17.03f, 38.898f, 16.927f)
                    curveTo(38.867f, 16.83f, 38.834f, 16.734f, 38.802f, 16.638f)
                    curveTo(38.773f, 16.556f, 38.743f, 16.476f, 38.715f, 16.394f)
                    curveTo(38.643f, 16.199f, 38.566f, 16.009f, 38.486f, 15.82f)
                    curveTo(38.466f, 15.772f, 38.446f, 15.724f, 38.424f, 15.676f)
                    curveTo(38.374f, 15.562f, 38.32f, 15.448f, 38.265f, 15.336f)
                    curveTo(38.243f, 15.29f, 38.219f, 15.244f, 38.198f, 15.2f)
                    curveTo(38.01f, 14.825f, 37.805f, 14.466f, 37.581f, 14.118f)
                    curveTo(37.567f, 14.096f, 37.552f, 14.074f, 37.539f, 14.052f)
                    curveTo(36.998f, 13.224f, 36.361f, 12.472f, 35.635f, 11.802f)
                    curveTo(35.605f, 11.773f, 35.574f, 11.747f, 35.541f, 11.718f)
                    curveTo(35.391f, 11.582f, 35.238f, 11.451f, 35.081f, 11.322f)
                    curveTo(35.038f, 11.287f, 34.996f, 11.252f, 34.953f, 11.217f)
                    curveTo(34.8f, 11.094f, 34.643f, 10.976f, 34.484f, 10.859f)
                    curveTo(34.442f, 10.829f, 34.401f, 10.798f, 34.357f, 10.767f)
                    curveTo(34.174f, 10.638f, 33.989f, 10.513f, 33.799f, 10.393f)
                    curveTo(33.782f, 10.382f, 33.764f, 10.368f, 33.744f, 10.358f)
                    curveTo(33.319f, 10.09f, 32.874f, 9.845f, 32.416f, 9.621f)
                    curveTo(32.364f, 9.597f, 32.312f, 9.573f, 32.262f, 9.549f)
                    curveTo(32.087f, 9.466f, 31.91f, 9.387f, 31.732f, 9.31f)
                    curveTo(31.664f, 9.282f, 31.594f, 9.253f, 31.524f, 9.225f)
                    curveTo(31.357f, 9.157f, 31.184f, 9.091f, 31.012f, 9.027f)
                    curveTo(30.94f, 9.001f, 30.87f, 8.975f, 30.796f, 8.949f)
                    curveTo(30.608f, 8.883f, 30.421f, 8.821f, 30.231f, 8.764f)
                    curveTo(30.174f, 8.747f, 30.118f, 8.727f, 30.061f, 8.712f)
                    curveTo(29.81f, 8.637f, 29.555f, 8.567f, 29.298f, 8.506f)
                    curveTo(29.291f, 8.506f, 29.287f, 8.504f, 29.28f, 8.502f)
                    curveTo(29.034f, 8.442f, 28.785f, 8.388f, 28.535f, 8.339f)
                    curveTo(28.456f, 8.324f, 28.375f, 8.311f, 28.295f, 8.296f)
                    curveTo(28.114f, 8.263f, 27.933f, 8.232f, 27.75f, 8.206f)
                    curveTo(27.654f, 8.193f, 27.56f, 8.179f, 27.464f, 8.166f)
                    curveTo(27.292f, 8.144f, 27.117f, 8.122f, 26.943f, 8.105f)
                    curveTo(26.844f, 8.094f, 26.746f, 8.085f, 26.648f, 8.076f)
                    curveTo(26.463f, 8.061f, 26.278f, 8.048f, 26.09f, 8.037f)
                    curveTo(26.003f, 8.033f, 25.918f, 8.026f, 25.83f, 8.022f)
                    curveTo(25.558f, 8.011f, 25.281f, 8.002f, 25.004f, 8.002f)
                    curveTo(24.727f, 8.002f, 24.452f, 8.009f, 24.177f, 8.022f)
                    curveTo(24.09f, 8.026f, 24.005f, 8.033f, 23.918f, 8.037f)
                    curveTo(23.73f, 8.048f, 23.545f, 8.059f, 23.36f, 8.076f)
                    curveTo(23.261f, 8.085f, 23.163f, 8.096f, 23.065f, 8.105f)
                    curveTo(22.891f, 8.122f, 22.716f, 8.144f, 22.544f, 8.166f)
                    curveTo(22.448f, 8.179f, 22.352f, 8.193f, 22.258f, 8.206f)
                    curveTo(22.075f, 8.232f, 21.894f, 8.265f, 21.713f, 8.296f)
                    curveTo(21.632f, 8.311f, 21.554f, 8.324f, 21.473f, 8.339f)
                    curveTo(21.222f, 8.388f, 20.974f, 8.442f, 20.727f, 8.502f)
                    curveTo(20.721f, 8.502f, 20.716f, 8.504f, 20.71f, 8.506f)
                    curveTo(20.453f, 8.569f, 20.198f, 8.637f, 19.947f, 8.712f)
                    curveTo(19.89f, 8.729f, 19.833f, 8.747f, 19.777f, 8.764f)
                    curveTo(19.587f, 8.824f, 19.397f, 8.885f, 19.212f, 8.949f)
                    curveTo(19.14f, 8.975f, 19.068f, 9.001f, 18.996f, 9.027f)
                    curveTo(18.824f, 9.091f, 18.653f, 9.157f, 18.483f, 9.225f)
                    curveTo(18.414f, 9.253f, 18.344f, 9.282f, 18.276f, 9.31f)
                    curveTo(18.097f, 9.387f, 17.921f, 9.466f, 17.749f, 9.547f)
                    curveTo(17.696f, 9.571f, 17.644f, 9.595f, 17.591f, 9.619f)
                    curveTo(17.131f, 9.843f, 16.689f, 10.088f, 16.263f, 10.355f)
                    curveTo(16.246f, 10.366f, 16.229f, 10.379f, 16.209f, 10.39f)
                    curveTo(16.019f, 10.511f, 15.834f, 10.636f, 15.651f, 10.765f)
                    curveTo(15.609f, 10.796f, 15.566f, 10.826f, 15.524f, 10.857f)
                    curveTo(15.365f, 10.973f, 15.208f, 11.092f, 15.055f, 11.214f)
                    curveTo(15.012f, 11.249f, 14.968f, 11.284f, 14.927f, 11.319f)
                    curveTo(14.769f, 11.449f, 14.617f, 11.58f, 14.466f, 11.716f)
                    curveTo(14.436f, 11.745f, 14.403f, 11.771f, 14.373f, 11.799f)
                    curveTo(13.649f, 12.47f, 13.01f, 13.222f, 12.469f, 14.05f)
                    curveTo(12.456f, 14.072f, 12.44f, 14.094f, 12.427f, 14.116f)
                    curveTo(12.205f, 14.464f, 11.998f, 14.823f, 11.81f, 15.198f)
                    curveTo(11.788f, 15.244f, 11.764f, 15.288f, 11.743f, 15.334f)
                    curveTo(11.688f, 15.446f, 11.634f, 15.559f, 11.583f, 15.674f)
                    curveTo(11.562f, 15.722f, 11.542f, 15.77f, 11.522f, 15.818f)
                    curveTo(11.442f, 16.007f, 11.365f, 16.197f, 11.293f, 16.392f)
                    curveTo(11.263f, 16.473f, 11.234f, 16.554f, 11.206f, 16.635f)
                    curveTo(11.173f, 16.732f, 11.141f, 16.828f, 11.11f, 16.925f)
                    curveTo(11.078f, 17.028f, 11.047f, 17.133f, 11.016f, 17.238f)
                    curveTo(10.99f, 17.332f, 10.962f, 17.424f, 10.938f, 17.521f)
                    curveTo(10.894f, 17.683f, 10.855f, 17.845f, 10.82f, 18.009f)
                    curveTo(10.803f, 18.086f, 10.787f, 18.16f, 10.772f, 18.237f)
                    curveTo(10.744f, 18.373f, 10.72f, 18.511f, 10.696f, 18.649f)
                    curveTo(10.685f, 18.715f, 10.672f, 18.781f, 10.663f, 18.849f)
                    curveTo(10.633f, 19.048f, 10.606f, 19.25f, 10.585f, 19.451f)
                    curveTo(10.58f, 19.502f, 10.576f, 19.552f, 10.571f, 19.602f)
                    curveTo(10.556f, 19.771f, 10.543f, 19.942f, 10.532f, 20.115f)
                    curveTo(10.528f, 20.177f, 10.526f, 20.238f, 10.521f, 20.301f)
                    curveTo(10.51f, 20.521f, 10.504f, 20.742f, 10.504f, 20.965f)
                    curveTo(10.504f, 22.12f, 10.637f, 23.275f, 10.89f, 24.408f)
                    curveTo(11.647f, 27.806f, 13.474f, 31.003f, 15.976f, 33.344f)
                    verticalLineTo(37.956f)
                    curveTo(15.976f, 39.637f, 17.332f, 41.0f, 19.005f, 41.0f)
                    curveTo(20.348f, 41.0f, 21.484f, 40.121f, 21.881f, 38.905f)
                    curveTo(21.885f, 38.894f, 21.888f, 38.883f, 21.89f, 38.872f)
                    curveTo(21.914f, 38.795f, 21.936f, 38.719f, 21.953f, 38.64f)
                    curveTo(21.964f, 38.594f, 21.97f, 38.543f, 21.979f, 38.497f)
                    curveTo(21.988f, 38.453f, 21.997f, 38.412f, 22.003f, 38.368f)
                    curveTo(22.204f, 39.854f, 23.469f, 41.0f, 25.002f, 41.0f)
                    curveTo(26.535f, 41.0f, 27.8f, 39.854f, 28.0f, 38.368f)
                    curveTo(28.009f, 38.425f, 28.02f, 38.482f, 28.031f, 38.539f)
                    curveTo(28.037f, 38.574f, 28.042f, 38.609f, 28.051f, 38.642f)
                    curveTo(28.068f, 38.723f, 28.092f, 38.8f, 28.116f, 38.879f)
                    curveTo(28.118f, 38.887f, 28.12f, 38.898f, 28.125f, 38.907f)
                    curveTo(28.521f, 40.123f, 29.658f, 41.002f, 31.001f, 41.002f)
                    curveTo(32.674f, 41.002f, 34.03f, 39.639f, 34.03f, 37.958f)
                    verticalLineTo(33.346f)
                    curveTo(36.532f, 31.006f, 38.359f, 27.811f, 39.116f, 24.41f)
                    curveTo(39.369f, 23.277f, 39.502f, 22.12f, 39.502f, 20.968f)
                    curveTo(39.502f, 20.744f, 39.495f, 20.523f, 39.484f, 20.303f)
                    horizontalLineTo(39.486f)
                    close()
                    moveTo(28.149f, 28.672f)
                    curveTo(27.922f, 28.396f, 27.734f, 28.087f, 27.593f, 27.752f)
                    curveTo(27.405f, 27.305f, 27.3f, 26.816f, 27.3f, 26.301f)
                    curveTo(27.3f, 25.786f, 27.405f, 25.295f, 27.593f, 24.85f)
                    curveTo(27.734f, 24.515f, 27.922f, 24.206f, 28.149f, 23.93f)
                    curveTo(28.829f, 23.102f, 29.861f, 22.574f, 31.012f, 22.574f)
                    curveTo(33.062f, 22.574f, 34.724f, 24.243f, 34.724f, 26.303f)
                    curveTo(34.724f, 28.363f, 33.062f, 30.033f, 31.012f, 30.033f)
                    curveTo(29.858f, 30.033f, 28.829f, 29.504f, 28.149f, 28.676f)
                    verticalLineTo(28.672f)
                    close()
                    moveTo(23.371f, 33.841f)
                    curveTo(23.371f, 32.934f, 24.101f, 32.2f, 25.004f, 32.2f)
                    curveTo(25.907f, 32.2f, 26.637f, 32.934f, 26.637f, 33.841f)
                    curveTo(26.637f, 34.748f, 25.907f, 35.482f, 25.004f, 35.482f)
                    curveTo(24.101f, 35.482f, 23.371f, 34.748f, 23.371f, 33.841f)
                    close()
                    moveTo(22.537f, 27.41f)
                    curveTo(22.069f, 28.928f, 20.658f, 30.03f, 18.994f, 30.03f)
                    curveTo(16.944f, 30.03f, 15.282f, 28.361f, 15.282f, 26.301f)
                    curveTo(15.282f, 24.241f, 16.944f, 22.572f, 18.994f, 22.572f)
                    curveTo(20.66f, 22.572f, 22.069f, 23.674f, 22.537f, 25.192f)
                    curveTo(22.646f, 25.543f, 22.705f, 25.915f, 22.705f, 26.301f)
                    curveTo(22.705f, 26.687f, 22.646f, 27.059f, 22.537f, 27.41f)
                    close()
                }
            }
        }
        .build()
        return _icEmojiSkull!!
    }

private var _icEmojiSkull: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcEmojiSkull, contentDescription = "")
    }
}
