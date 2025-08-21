package kr.co.architecture.core.ui.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.domain.CalculateDateBeforeUseCase
import kr.co.architecture.core.domain.CalculateDateBeforeUseCase.BeforeTimeUnit
import kr.co.architecture.core.model.enums.BuddyStatus
import kr.co.architecture.core.repository.dto.BuddyRequestFindAllItemResponseDto
import kr.co.architecture.core.ui.BaseBuddyInfoUiModel
import kr.co.architecture.core.ui.BaseBuddyInfoWithBuddyRequestUiModel
import kr.co.architecture.core.ui.R
import kr.co.architecture.core.ui.enums.BuddyTypeEnum
import kr.co.architecture.core.ui.enums.EmojiEnum
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.ui.util.plus
import kr.co.architecture.core.ui.R as coreUiR

data class ReceivingBuddyListUiModel(
  val receivingBuddyListUiModel: ImmutableList<ReceivingBuddyUiModel> = persistentListOf()
) {
  companion object {
    fun mapperToUiModel(
      dto: BuddyRequestFindAllItemResponseDto,
      calculateDateBeforeUseCase: CalculateDateBeforeUseCase,
      ) = dto.list
      .map {
        ReceivingBuddyUiModel(
          baseBuddyInfoWithBuddyRequestUiModel = BaseBuddyInfoWithBuddyRequestUiModel(
            baseBuddyInfoUiModel = BaseBuddyInfoUiModel(
              userId = it.userId,
              emojiEnum = EmojiEnum.creator(it.imogiCd),
              titleText = UiText.DynamicString(
                when (BuddyTypeEnum.creator(it.buddyType)) {
                  BuddyTypeEnum.NICKNAME -> it.nickname ?: ""
                  BuddyTypeEnum.CONTACT -> it.username ?: ""
                  BuddyTypeEnum.NONE -> it.nickname ?: ""
                  else -> ""
                }
              ),
              buddyType = BuddyTypeEnum.creator(it.buddyType),
              contentText = run {
                val dateText = with(calculateDateBeforeUseCase(CalculateDateBeforeUseCase.Params(it.requestDate))) {
                  UiText.StringResource(
                    resId = when (beforeTimeUnit) {
                      BeforeTimeUnit.YEAR -> coreUiR.string.before_year
                      BeforeTimeUnit.MONTH -> coreUiR.string.before_month
                      BeforeTimeUnit.WEEK -> coreUiR.string.before_week
                      BeforeTimeUnit.DAY -> coreUiR.string.before_day
                      BeforeTimeUnit.HOUR -> coreUiR.string.before_hour
                      BeforeTimeUnit.MINUTE -> coreUiR.string.before_minute
                      BeforeTimeUnit.NOW -> coreUiR.string.now
                    },
                    args = listOf(beforeTime)
                  )
                }
                val countText = UiText.StringResource(
                  R.string.alarm_buddy_with_cnt_unit,
                  listOf(it.withBuddyCount)
                )

                val contentText =
                  if (it.withBuddyCount > 0) countText + "ㆍ"
                  else UiText.DynamicString("")

                contentText + dateText
              }
            ),
            buddyStatus = BuddyStatus.WAITING_MY_ACCEPT
          ),
          buddyRequestId = it.buddyRequestId,
        )
      }
      .toImmutableList()
      .let(::ReceivingBuddyListUiModel)
  }
  data class ReceivingBuddyUiModel(
    val baseBuddyInfoWithBuddyRequestUiModel: BaseBuddyInfoWithBuddyRequestUiModel,
    val buddyRequestId: Int
  )
}