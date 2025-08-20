package kr.co.architecture.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.model.enums.BuddyStatus

data class BaseBuddyInfoWithBuddyRequestUiModel(
  val baseBuddyInfoUiModel: BaseBuddyInfoUiModel,
  val buddyStatus: BuddyStatus
)

// TODO: 아래 모듈로 공통부분 대채ㅔ 필요
@Composable
fun BaseBuddyInfoWithBuddyRequestSection(
  modifier: Modifier = Modifier,
  emojiSize: Dp = 40.dp,
  baseBuddyInfoWithBuddyRequestUiModel: BaseBuddyInfoWithBuddyRequestUiModel,
  onClickedBuddyItem: () -> Unit = {},
  onClickedBuddyRequest: () -> Unit = {}
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    BaseBuddyInfoSection(
      modifier = Modifier
          .weight(0.7f)
          .noRippledClickable(onClick = onClickedBuddyItem),
      emojiSize = emojiSize,
      baseBuddyInfoUiModel = baseBuddyInfoWithBuddyRequestUiModel.baseBuddyInfoUiModel
    )

    BuddyRequestButton(
      modifier = Modifier
        .weight(0.3f),
      buddyStatus = baseBuddyInfoWithBuddyRequestUiModel.buddyStatus,
      onClickedBuddyRequest = onClickedBuddyRequest
    )
  }
}