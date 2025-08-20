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
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val IcBadgeStar: ImageVector
    get() {
        if (_badgeStar != null) {
            return _badgeStar!!
        }
        _badgeStar = Builder(name = "BadgeStar", defaultWidth = 12.0.dp, defaultHeight = 12.0.dp,
                viewportWidth = 12.0f, viewportHeight = 12.0f).apply {
            group {
            }
            group {
                path(fill = SolidColor(Color(0xFFFFB92D)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(6.0f, 6.0f)
                    moveToRelative(-6.0f, 0.0f)
                    arcToRelative(6.0f, 6.0f, 0.0f, true, true, 12.0f, 0.0f)
                    arcToRelative(6.0f, 6.0f, 0.0f, true, true, -12.0f, 0.0f)
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(5.576f, 2.775f)
                    curveTo(5.75f, 2.408f, 6.25f, 2.408f, 6.424f, 2.775f)
                    lineTo(7.249f, 4.521f)
                    lineTo(9.095f, 4.801f)
                    curveTo(9.482f, 4.86f, 9.637f, 5.358f, 9.357f, 5.643f)
                    lineTo(8.021f, 7.003f)
                    lineTo(8.337f, 8.922f)
                    curveTo(8.403f, 9.325f, 7.998f, 9.633f, 7.651f, 9.442f)
                    lineTo(6.0f, 8.536f)
                    lineTo(4.349f, 9.442f)
                    curveTo(4.002f, 9.633f, 3.597f, 9.325f, 3.663f, 8.922f)
                    lineTo(3.979f, 7.003f)
                    lineTo(2.643f, 5.643f)
                    curveTo(2.363f, 5.358f, 2.518f, 4.86f, 2.905f, 4.801f)
                    lineTo(4.751f, 4.521f)
                    lineTo(5.576f, 2.775f)
                    close()
                }
            }
        }
        .build()
        return _badgeStar!!
    }

private var _badgeStar: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = IcBadgeStar, contentDescription = "")
    }
}
