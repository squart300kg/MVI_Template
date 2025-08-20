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

val IcBadgeNew: ImageVector
    get() {
        if (_badgeNew != null) {
            return _badgeNew!!
        }
        _badgeNew = Builder(name = "BadgeNew", defaultWidth = 12.0.dp, defaultHeight = 12.0.dp,
                viewportWidth = 12.0f, viewportHeight = 12.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFED373B)),
                    strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(6.0f, 6.0f)
                moveToRelative(-5.5f, 0.0f)
                arcToRelative(5.5f, 5.5f, 0.0f, true, true, 11.0f, 0.0f)
                arcToRelative(5.5f, 5.5f, 0.0f, true, true, -11.0f, 0.0f)
            }
            path(fill = SolidColor(Color(0xFFED373B)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(3.5f, 9.0f)
                verticalLineTo(3.0f)
                horizontalLineTo(4.181f)
                lineTo(8.027f, 7.929f)
                horizontalLineTo(7.669f)
                verticalLineTo(3.0f)
                horizontalLineTo(8.5f)
                verticalLineTo(9.0f)
                horizontalLineTo(7.819f)
                lineTo(3.973f, 4.071f)
                horizontalLineTo(4.331f)
                verticalLineTo(9.0f)
                horizontalLineTo(3.5f)
                close()
            }
        }
        .build()
        return _badgeNew!!
    }

private var _badgeNew: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcBadgeNew, contentDescription = "")
    }
}
