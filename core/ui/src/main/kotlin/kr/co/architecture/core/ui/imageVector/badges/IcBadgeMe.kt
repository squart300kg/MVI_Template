package com.pickstudio.buddystock.views.ui.icon.badgeIcon

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

val IcBadgeMe: ImageVector
    get() {
        if (_icBadgeMe != null) {
            return _icBadgeMe!!
        }
        _icBadgeMe = Builder(name = "IcBadgeMe", defaultWidth = 30.0.dp, defaultHeight = 14.0.dp,
                viewportWidth = 30.0f, viewportHeight = 14.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF00AEB0)),
                    strokeLineWidth = 0.5f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(7.0f, 0.25f)
                lineTo(23.0f, 0.25f)
                arcTo(6.75f, 6.75f, 0.0f, false, true, 29.75f, 7.0f)
                lineTo(29.75f, 7.0f)
                arcTo(6.75f, 6.75f, 0.0f, false, true, 23.0f, 13.75f)
                lineTo(7.0f, 13.75f)
                arcTo(6.75f, 6.75f, 0.0f, false, true, 0.25f, 7.0f)
                lineTo(0.25f, 7.0f)
                arcTo(6.75f, 6.75f, 0.0f, false, true, 7.0f, 0.25f)
                close()
            }
            path(fill = SolidColor(Color(0xFF00AEB0)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(10.453f, 5.848f)
                verticalLineTo(7.744f)
                horizontalLineTo(9.797f)
                verticalLineTo(3.424f)
                horizontalLineTo(10.453f)
                verticalLineTo(5.304f)
                horizontalLineTo(11.517f)
                verticalLineTo(5.848f)
                horizontalLineTo(10.453f)
                close()
                moveTo(8.717f, 7.44f)
                curveTo(8.317f, 7.301f, 7.968f, 7.093f, 7.669f, 6.816f)
                curveTo(7.37f, 6.533f, 7.144f, 6.208f, 6.989f, 5.84f)
                curveTo(6.834f, 6.24f, 6.602f, 6.592f, 6.293f, 6.896f)
                curveTo(5.984f, 7.2f, 5.618f, 7.429f, 5.197f, 7.584f)
                lineTo(4.853f, 7.064f)
                curveTo(5.125f, 6.968f, 5.37f, 6.843f, 5.589f, 6.688f)
                curveTo(5.813f, 6.528f, 6.002f, 6.347f, 6.157f, 6.144f)
                curveTo(6.317f, 5.941f, 6.44f, 5.72f, 6.525f, 5.48f)
                curveTo(6.61f, 5.24f, 6.653f, 4.992f, 6.653f, 4.736f)
                verticalLineTo(4.448f)
                horizontalLineTo(5.045f)
                verticalLineTo(3.92f)
                horizontalLineTo(8.893f)
                verticalLineTo(4.448f)
                horizontalLineTo(7.309f)
                verticalLineTo(4.728f)
                curveTo(7.309f, 4.968f, 7.349f, 5.2f, 7.429f, 5.424f)
                curveTo(7.514f, 5.648f, 7.632f, 5.856f, 7.781f, 6.048f)
                curveTo(7.936f, 6.24f, 8.12f, 6.413f, 8.333f, 6.568f)
                curveTo(8.552f, 6.717f, 8.792f, 6.837f, 9.053f, 6.928f)
                lineTo(8.717f, 7.44f)
                close()
                moveTo(10.453f, 8.128f)
                verticalLineTo(10.6f)
                horizontalLineTo(9.797f)
                verticalLineTo(8.656f)
                horizontalLineTo(5.781f)
                verticalLineTo(8.128f)
                horizontalLineTo(10.453f)
                close()
                moveTo(15.432f, 5.48f)
                verticalLineTo(4.944f)
                horizontalLineTo(16.992f)
                verticalLineTo(3.424f)
                horizontalLineTo(17.656f)
                verticalLineTo(7.664f)
                horizontalLineTo(16.992f)
                verticalLineTo(5.48f)
                horizontalLineTo(15.432f)
                close()
                moveTo(15.6f, 7.328f)
                curveTo(15.2f, 7.179f, 14.854f, 6.968f, 14.56f, 6.696f)
                curveTo(14.272f, 6.419f, 14.051f, 6.093f, 13.896f, 5.72f)
                curveTo(13.742f, 6.152f, 13.507f, 6.523f, 13.192f, 6.832f)
                curveTo(12.883f, 7.141f, 12.512f, 7.379f, 12.08f, 7.544f)
                lineTo(11.736f, 7.008f)
                curveTo(12.014f, 6.912f, 12.264f, 6.781f, 12.488f, 6.616f)
                curveTo(12.712f, 6.451f, 12.902f, 6.261f, 13.056f, 6.048f)
                curveTo(13.216f, 5.835f, 13.339f, 5.603f, 13.424f, 5.352f)
                curveTo(13.51f, 5.096f, 13.552f, 4.832f, 13.552f, 4.56f)
                verticalLineTo(3.824f)
                horizontalLineTo(14.208f)
                verticalLineTo(4.536f)
                curveTo(14.208f, 4.792f, 14.248f, 5.037f, 14.328f, 5.272f)
                curveTo(14.414f, 5.507f, 14.531f, 5.723f, 14.68f, 5.92f)
                curveTo(14.835f, 6.117f, 15.019f, 6.293f, 15.232f, 6.448f)
                curveTo(15.451f, 6.603f, 15.691f, 6.725f, 15.952f, 6.816f)
                lineTo(15.6f, 7.328f)
                close()
                moveTo(15.288f, 7.88f)
                curveTo(15.656f, 7.88f, 15.987f, 7.909f, 16.28f, 7.968f)
                curveTo(16.574f, 8.027f, 16.824f, 8.115f, 17.032f, 8.232f)
                curveTo(17.24f, 8.349f, 17.398f, 8.491f, 17.504f, 8.656f)
                curveTo(17.616f, 8.821f, 17.672f, 9.013f, 17.672f, 9.232f)
                curveTo(17.672f, 9.445f, 17.616f, 9.637f, 17.504f, 9.808f)
                curveTo(17.398f, 9.973f, 17.24f, 10.112f, 17.032f, 10.224f)
                curveTo(16.824f, 10.341f, 16.574f, 10.429f, 16.28f, 10.488f)
                curveTo(15.987f, 10.552f, 15.656f, 10.584f, 15.288f, 10.584f)
                curveTo(14.92f, 10.584f, 14.587f, 10.552f, 14.288f, 10.488f)
                curveTo(13.995f, 10.429f, 13.744f, 10.341f, 13.536f, 10.224f)
                curveTo(13.334f, 10.112f, 13.176f, 9.973f, 13.064f, 9.808f)
                curveTo(12.952f, 9.637f, 12.896f, 9.445f, 12.896f, 9.232f)
                curveTo(12.896f, 9.013f, 12.952f, 8.821f, 13.064f, 8.656f)
                curveTo(13.176f, 8.491f, 13.334f, 8.349f, 13.536f, 8.232f)
                curveTo(13.744f, 8.115f, 13.995f, 8.027f, 14.288f, 7.968f)
                curveTo(14.587f, 7.909f, 14.92f, 7.88f, 15.288f, 7.88f)
                close()
                moveTo(15.288f, 10.064f)
                curveTo(15.838f, 10.064f, 16.264f, 9.992f, 16.568f, 9.848f)
                curveTo(16.872f, 9.699f, 17.024f, 9.493f, 17.024f, 9.232f)
                curveTo(17.024f, 8.96f, 16.872f, 8.752f, 16.568f, 8.608f)
                curveTo(16.264f, 8.464f, 15.838f, 8.392f, 15.288f, 8.392f)
                curveTo(14.739f, 8.392f, 14.312f, 8.464f, 14.008f, 8.608f)
                curveTo(13.704f, 8.752f, 13.552f, 8.96f, 13.552f, 9.232f)
                curveTo(13.552f, 9.493f, 13.704f, 9.699f, 14.008f, 9.848f)
                curveTo(14.312f, 9.992f, 14.739f, 10.064f, 15.288f, 10.064f)
                close()
                moveTo(24.124f, 6.864f)
                verticalLineTo(10.592f)
                horizontalLineTo(23.468f)
                verticalLineTo(3.424f)
                horizontalLineTo(24.124f)
                verticalLineTo(6.312f)
                horizontalLineTo(25.308f)
                verticalLineTo(6.864f)
                horizontalLineTo(24.124f)
                close()
                moveTo(21.02f, 5.608f)
                curveTo(21.02f, 5.896f, 21.068f, 6.187f, 21.164f, 6.48f)
                curveTo(21.26f, 6.768f, 21.39f, 7.043f, 21.556f, 7.304f)
                curveTo(21.726f, 7.565f, 21.924f, 7.803f, 22.148f, 8.016f)
                curveTo(22.372f, 8.224f, 22.614f, 8.389f, 22.876f, 8.512f)
                lineTo(22.5f, 9.04f)
                curveTo(22.302f, 8.939f, 22.11f, 8.813f, 21.924f, 8.664f)
                curveTo(21.742f, 8.509f, 21.574f, 8.339f, 21.42f, 8.152f)
                curveTo(21.265f, 7.96f, 21.126f, 7.755f, 21.004f, 7.536f)
                curveTo(20.881f, 7.312f, 20.78f, 7.08f, 20.7f, 6.84f)
                curveTo(20.62f, 7.101f, 20.516f, 7.355f, 20.388f, 7.6f)
                curveTo(20.265f, 7.84f, 20.124f, 8.064f, 19.964f, 8.272f)
                curveTo(19.804f, 8.475f, 19.63f, 8.659f, 19.444f, 8.824f)
                curveTo(19.262f, 8.984f, 19.07f, 9.115f, 18.868f, 9.216f)
                lineTo(18.484f, 8.696f)
                curveTo(18.74f, 8.568f, 18.982f, 8.392f, 19.212f, 8.168f)
                curveTo(19.441f, 7.944f, 19.641f, 7.693f, 19.812f, 7.416f)
                curveTo(19.982f, 7.139f, 20.118f, 6.845f, 20.22f, 6.536f)
                curveTo(20.321f, 6.221f, 20.372f, 5.912f, 20.372f, 5.608f)
                verticalLineTo(4.696f)
                horizontalLineTo(18.732f)
                verticalLineTo(4.152f)
                horizontalLineTo(22.612f)
                verticalLineTo(4.696f)
                horizontalLineTo(21.02f)
                verticalLineTo(5.608f)
                close()
            }
        }
        .build()
        return _icBadgeMe!!
    }

private var _icBadgeMe: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcBadgeMe, contentDescription = "")
    }
}
