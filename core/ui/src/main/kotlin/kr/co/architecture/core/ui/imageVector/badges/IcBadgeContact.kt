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

val IcBadgeContact: ImageVector
    get() {
        if (_badgeContact != null) {
            return _badgeContact!!
        }
        _badgeContact = Builder(name = "BadgeContact", defaultWidth = 12.0.dp, defaultHeight =
                12.0.dp, viewportWidth = 12.0f, viewportHeight = 12.0f).apply {
            path(fill = SolidColor(Color(0xFF7A869A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(6.0f, 1.0f)
                curveTo(4.895f, 1.0f, 4.0f, 1.895f, 4.0f, 3.0f)
                curveTo(4.0f, 4.105f, 4.895f, 5.0f, 6.0f, 5.0f)
                curveTo(7.105f, 5.0f, 8.0f, 4.105f, 8.0f, 3.0f)
                curveTo(8.0f, 1.895f, 7.105f, 1.0f, 6.0f, 1.0f)
                close()
                moveTo(8.5f, 6.0f)
                lineTo(3.5f, 6.0f)
                curveTo(2.672f, 6.0f, 2.0f, 6.672f, 2.0f, 7.5f)
                curveTo(2.0f, 8.616f, 2.459f, 9.51f, 3.212f, 10.115f)
                curveTo(3.953f, 10.71f, 4.947f, 11.0f, 6.0f, 11.0f)
                curveTo(7.053f, 11.0f, 8.047f, 10.71f, 8.788f, 10.115f)
                curveTo(9.541f, 9.51f, 10.0f, 8.616f, 10.0f, 7.5f)
                curveTo(10.0f, 6.672f, 9.328f, 6.0f, 8.5f, 6.0f)
                close()
            }
        }
        .build()
        return _badgeContact!!
    }

private var _badgeContact: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcBadgeContact, contentDescription = "")
    }
}
