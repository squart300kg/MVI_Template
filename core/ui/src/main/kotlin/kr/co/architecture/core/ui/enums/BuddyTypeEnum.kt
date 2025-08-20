package kr.co.architecture.core.ui.enums

import androidx.compose.ui.graphics.vector.ImageVector
import com.pickstudio.buddystock.views.ui.icon.badgeIcon.IcBadgeCommunity
import com.pickstudio.buddystock.views.ui.icon.badgeIcon.IcBadgeContact
import com.pickstudio.buddystock.views.ui.icon.badgeIcon.IcBadgeYoutube

enum class BuddyTypeEnum(
    val str: String,
    val vectorImage: ImageVector? = null
) {
    MY("MY"),
    NICKNAME("BR0101"),// 버디 유형 닉네임
    CONTACT("BR0102", IcBadgeContact), // 버디 유형 연착처
    YOUTUBER("BR0103", IcBadgeYoutube), // 버디 유형 유튜버
    NONE("NONE", IcBadgeCommunity);

    companion object {
        fun creator(value: String?): BuddyTypeEnum =
            BuddyTypeEnum.entries.firstOrNull { it.str == value } ?: NONE

        fun creatorName(value: String?): BuddyTypeEnum =
            BuddyTypeEnum.entries.firstOrNull { it.name == value } ?: NONE

    }
}

