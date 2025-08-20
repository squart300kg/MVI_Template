package kr.co.architecture.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.ui.enums.BuddyTypeEnum
import kr.co.architecture.core.ui.enums.EmojiEnum
import kr.co.architecture.core.ui.preview.BaseBuddyInfoUiModelPreviewParam
import kr.co.architecture.core.ui.theme.BaseTheme
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.asString
import kr.co.architecture.core.ui.theme.*

const val BASE_BUDDY_VO = "baseBuddyVo"
@Immutable
data class BaseBuddyInfoUiModel(
  val userId: Int = -1,
  val emojiEnum: EmojiEnum = EmojiEnum.NONE,
  val titleText: UiText = UiText.DynamicString(""),
  val buddyType: BuddyTypeEnum = BuddyTypeEnum.NONE,
  val contentText: UiText = UiText.DynamicString("")
)

@Composable
fun BaseBuddyInfoSection(
  modifier: Modifier = Modifier,
  baseBuddyInfoUiModel: BaseBuddyInfoUiModel,
  emojiSize: Dp = 40.dp
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    baseBuddyInfoUiModel.emojiEnum.imageVector?.let { imageVector ->
      Image(
        modifier = Modifier
          .size(emojiSize),
        imageVector = imageVector,
        contentDescription = null
      )
    } ?: run {
      Box(
        modifier = Modifier
          .size(emojiSize)
      )
    }

    Column(
      modifier = Modifier
        .padding(start = 12.dp)
    ) {

      Row {
        Text(
          text = baseBuddyInfoUiModel.titleText.asString(),
          style = TypoSubhead2.copy(
            color = colorResource(id = R.color.gray_091E42),
            fontWeight = FontWeight.W500
          )
        )

        baseBuddyInfoUiModel.buddyType.vectorImage?.let { vectorImage ->
          Image(
            modifier = Modifier
              .padding(start = 3.dp)
              .size(12.dp)
              .align(Alignment.CenterVertically),
            imageVector = vectorImage,
            contentDescription = null
          )
        }
      }

      if (baseBuddyInfoUiModel.contentText.asString().isNotEmpty()) {
        Text(
          text = baseBuddyInfoUiModel.contentText.asString(),
          style = TypoCaption.copy(
            color = colorResource(id = R.color.gray_7A869A),
            fontWeight = FontWeight.W400
          )
        )
      }
    }
  }
}

@Preview
@Composable
fun BaseBuddyInfoSectionPreview(
  @PreviewParameter(BaseBuddyInfoUiModelPreviewParam::class)
  baseBuddyInfoUiModelPreviewParam: BaseBuddyInfoUiModel
) {
  BaseTheme {
    BaseBuddyInfoSection(
      baseBuddyInfoUiModel = baseBuddyInfoUiModelPreviewParam,
    )
  }
}