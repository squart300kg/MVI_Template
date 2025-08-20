package kr.co.architecture.core.ui

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import kr.co.architecture.core.model.enums.BuddyStatus
import kr.co.architecture.core.ui.theme.TypoCaption
import kotlin.let

@Immutable
data class BuddyAddBtn(
  val buddyStatus: BuddyStatus,
  @androidx.annotation.ColorRes val buttonColorRes: Int,
  @androidx.annotation.ColorRes val buttonTextColorRes: Int,
  @androidx.annotation.StringRes val buttonTextRes: Int,
  val buttonBorder: BorderStroke?,
)

@Composable
fun BuddyRequestButton(
  modifier: Modifier = Modifier,
  buddyStatus: BuddyStatus,
  onClickedBuddyRequest: () -> Unit
) {
  val buddyAddBtnState by rememberUpdatedState(
    newValue = when (buddyStatus) {
      BuddyStatus.NOT_BUDDY -> BuddyAddBtn(
        buddyStatus = buddyStatus,
        buttonColorRes = R.color.gray_091E42,
        buttonTextRes = R.string.btn_buddy_apply,
        buttonTextColorRes = R.color.white,
        buttonBorder = null
      )

      BuddyStatus.WAITING_BUDDY_ACCEPT -> BuddyAddBtn(
        buddyStatus = buddyStatus,
        buttonColorRes = R.color.white,
        buttonTextRes = R.string.btn_require_cancel,
        buttonTextColorRes = R.color.gray_091E42,
        buttonBorder = BorderStroke(
          width = 1.dp,
          color = colorResource(id = R.color.gray_091E42),
        )
      )

      BuddyStatus.WAITING_MY_ACCEPT -> BuddyAddBtn(
        buddyStatus = buddyStatus,
        buttonColorRes = R.color.gray_091E42,
        buttonTextRes = R.string.btn_accept,
        buttonTextColorRes = R.color.white,
        buttonBorder = BorderStroke(
          width = 1.dp,
          color = colorResource(id = R.color.white),
        )
      )

      else -> null
    }
  )
  // 버디 요청/해제 버튼
  buddyAddBtnState?.let { btnState ->
    BaseButton(
      modifier = modifier
        .height(32.dp),
      buttonColorRes = btnState.buttonColorRes,
      buttonTextRes = btnState.buttonTextRes,
      buttonTextStyle = TypoCaption.copy(
        color = colorResource(id = btnState.buttonTextColorRes)
      ),
      border = btnState.buttonBorder,
      onClick = onClickedBuddyRequest
    )
  }
}