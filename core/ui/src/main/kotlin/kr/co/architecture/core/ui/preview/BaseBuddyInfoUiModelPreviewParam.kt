package kr.co.architecture.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kr.co.architecture.core.ui.BaseBuddyInfoUiModel
import kr.co.architecture.core.ui.enums.BuddyTypeEnum
import kr.co.architecture.core.ui.enums.EmojiEnum
import kr.co.architecture.core.ui.util.UiText

class BaseBuddyInfoUiModelPreviewParam : PreviewParameterProvider<BaseBuddyInfoUiModel> {
  override val values: Sequence<BaseBuddyInfoUiModel> = sequenceOf(
    BaseBuddyInfoUiModel(
      userId = 11,
      emojiEnum = EmojiEnum.EC0101,
      titleText = UiText.DynamicString("SSY"),
      buddyType = BuddyTypeEnum.NICKNAME,
      contentText = UiText.DynamicString("10분 전")
    ),
    BaseBuddyInfoUiModel(
      userId = 11,
      emojiEnum = EmojiEnum.EC0101,
      titleText = UiText.DynamicString("SSY"),
      buddyType = BuddyTypeEnum.YOUTUBER,
      contentText = UiText.DynamicString("함께아는 버디 10명ㆍ10분 전")
    ),
    BaseBuddyInfoUiModel(
      userId = 11,
      emojiEnum = EmojiEnum.EC0101,
      titleText = UiText.DynamicString("SSY"),
      buddyType = BuddyTypeEnum.CONTACT,
      contentText = UiText.DynamicString("함께아는 버디 10명ㆍ10분 전")
    )
  )
}