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

val IcBadgeCommunity: ImageVector
    get() {
        if (_badgeCommunity != null) {
            return _badgeCommunity!!
        }
        _badgeCommunity = Builder(name = "BadgeCommunity", defaultWidth = 10.0.dp, defaultHeight =
                10.0.dp, viewportWidth = 10.0f, viewportHeight = 10.0f).apply {
            path(fill = SolidColor(Color(0xFF7A869A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(8.679f, 8.825f)
                curveTo(9.236f, 8.25f, 9.566f, 7.477f, 9.566f, 6.625f)
                curveTo(9.566f, 5.743f, 9.194f, 4.941f, 8.596f, 4.356f)
                curveTo(8.338f, 6.427f, 6.481f, 8.032f, 4.232f, 8.022f)
                horizontalLineTo(3.148f)
                curveTo(3.695f, 9.122f, 4.861f, 9.875f, 6.202f, 9.875f)
                lineTo(9.875f, 9.865f)
                lineTo(8.679f, 8.825f)
                close()
            }
            path(fill = SolidColor(Color(0xFF7A869A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(4.365f, 0.125f)
                curveTo(2.23f, 0.115f, 0.496f, 1.77f, 0.486f, 3.821f)
                curveTo(0.486f, 4.792f, 0.878f, 5.684f, 1.508f, 6.348f)
                lineTo(0.125f, 7.537f)
                lineTo(4.345f, 7.547f)
                curveTo(6.481f, 7.547f, 8.214f, 5.892f, 8.224f, 3.841f)
                curveTo(8.224f, 1.8f, 6.501f, 0.125f, 4.365f, 0.125f)
                close()
                moveTo(5.377f, 2.364f)
                curveTo(5.645f, 2.364f, 5.862f, 2.572f, 5.862f, 2.83f)
                curveTo(5.862f, 3.088f, 5.645f, 3.296f, 5.377f, 3.296f)
                curveTo(5.108f, 3.296f, 4.892f, 3.088f, 4.892f, 2.83f)
                curveTo(4.892f, 2.572f, 5.108f, 2.364f, 5.377f, 2.364f)
                close()
                moveTo(3.313f, 2.364f)
                curveTo(3.581f, 2.364f, 3.798f, 2.572f, 3.798f, 2.83f)
                curveTo(3.798f, 3.088f, 3.581f, 3.296f, 3.313f, 3.296f)
                curveTo(3.045f, 3.296f, 2.828f, 3.088f, 2.828f, 2.83f)
                curveTo(2.828f, 2.572f, 3.045f, 2.364f, 3.313f, 2.364f)
                close()
                moveTo(4.386f, 5.505f)
                horizontalLineTo(4.345f)
                curveTo(3.509f, 5.486f, 2.838f, 4.931f, 2.838f, 4.247f)
                curveTo(2.838f, 4.217f, 2.838f, 4.148f, 2.9f, 4.088f)
                curveTo(2.952f, 4.039f, 3.035f, 4.009f, 3.148f, 4.009f)
                curveTo(3.148f, 4.009f, 5.624f, 4.009f, 5.635f, 4.009f)
                curveTo(5.676f, 4.009f, 5.769f, 4.019f, 5.841f, 4.088f)
                curveTo(5.882f, 4.128f, 5.913f, 4.188f, 5.903f, 4.247f)
                curveTo(5.892f, 4.931f, 5.222f, 5.495f, 4.386f, 5.505f)
                close()
            }
        }
        .build()
        return _badgeCommunity!!
    }

private var _badgeCommunity: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcBadgeCommunity, contentDescription = "")
    }
}
