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

val IcBadgeYoutube: ImageVector
    get() {
        if (_badgeYoutube != null) {
            return _badgeYoutube!!
        }
        _badgeYoutube = Builder(name = "BadgeYoutube", defaultWidth = 12.0.dp, defaultHeight =
                12.0.dp, viewportWidth = 12.0f, viewportHeight = 12.0f).apply {
            path(fill = SolidColor(Color(0xFFFF522D)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(1.5f, 4.125f)
                curveTo(1.5f, 3.089f, 2.339f, 2.25f, 3.375f, 2.25f)
                horizontalLineTo(8.625f)
                curveTo(9.661f, 2.25f, 10.5f, 3.089f, 10.5f, 4.125f)
                verticalLineTo(7.875f)
                curveTo(10.5f, 8.911f, 9.661f, 9.75f, 8.625f, 9.75f)
                horizontalLineTo(3.375f)
                curveTo(2.339f, 9.75f, 1.5f, 8.911f, 1.5f, 7.875f)
                verticalLineTo(4.125f)
                close()
                moveTo(4.875f, 4.365f)
                verticalLineTo(7.635f)
                curveTo(4.875f, 7.822f, 5.08f, 7.937f, 5.24f, 7.839f)
                lineTo(7.685f, 6.346f)
                curveTo(7.943f, 6.188f, 7.943f, 5.812f, 7.685f, 5.654f)
                lineTo(5.24f, 4.161f)
                curveTo(5.08f, 4.063f, 4.875f, 4.178f, 4.875f, 4.365f)
                close()
            }
        }
        .build()
        return _badgeYoutube!!
    }

private var _badgeYoutube: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcBadgeYoutube, contentDescription = "")
    }
}
