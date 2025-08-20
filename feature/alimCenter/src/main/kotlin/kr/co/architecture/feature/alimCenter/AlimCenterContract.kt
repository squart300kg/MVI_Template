package kr.co.architecture.feature.alimCenter

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.co.architecture.core.domain.CalculateDateBeforeUseCase
import kr.co.architecture.core.domain.CalculateDateBeforeUseCase.BeforeTimeUnit
import kr.co.architecture.core.domain.ChangeFromYnToBoolean
import kr.co.architecture.core.ui.model.ReceivingBuddyListUiModel
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.enums.EmojiEnum
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.core.model.enums.AlimContentsEnum
import kr.co.architecture.core.ui.enums.BuddyTypeEnum
import kr.co.architecture.core.repository.dto.AlimListResponseDto
import kr.co.architecture.core.ui.BaseBuddyInfoUiModel
import kr.co.architecture.core.ui.CenterDialogUiModel
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiBuddyStockLogo
import kr.co.architecture.core.ui.R as coreUiR

data class AlimListUiModel(
  val noticeAlimListUiModel: ImmutableList<AlimUiModel> = persistentListOf(),
  val salesHistoryAlimListUiModel: ImmutableList<AlimUiModel> = persistentListOf(),
  val activityAlimListUiModel: ImmutableList<AlimUiModel> = persistentListOf(),
  val moreNotice: Boolean = false,
  val moreTrading: Boolean = false,
  val moreActivity: Boolean = false
) {
  data class AlimUiModel(
    val uiBindingModel: AlimUiBindingModel,
    val uiEventModel: AlimUiEventModel
  ) {
    data class AlimUiBindingModel(
      val emojiEnum: EmojiEnum? = null,
      val buddyStockEmoji: ImageVector? = null,
      val subCommentContents: UiText? = null,
      val moreViewUrl: String? = null,
      val name: UiText,
      val contents: UiText,
      val createdAt: UiText,
      val isNew: Boolean
    )

    data class AlimUiEventModel(
      val contentsType: AlimContentsEnum,
      val buddyType: BuddyTypeEnum? = null,
      val subCommentId: Int? = null,
      val buddyUserId: Int? = null,
      val commentId: Int? = null,
      val tradingId: Int? = null,
      val noticeId: Int? = null,
      val buddyCnt: Int? = null
    )
  }

  companion object {
    fun mapperToUiModel(
      dtos: List<AlimListResponseDto.AlimDto>,
      calculateDateBeforeUseCase: CalculateDateBeforeUseCase,
      changeFromYnToBoolean: ChangeFromYnToBoolean,
    ): ImmutableList<AlimUiModel> {
      return persistentListOf<AlimUiModel>()
        .addAll(
          dtos.map { dto ->
            when (dto) {
              is AlimListResponseDto.AlimDto.Notice -> {
                AlimUiModel(
                  uiBindingModel = AlimUiModel.AlimUiBindingModel(
                    buddyStockEmoji = IcEmojiBuddyStockLogo,
                    name = UiText.DynamicString(dto.nickname),
                    contents = UiText.DynamicString(dto.contents),
                    subCommentContents = dto.subcommentContent?.let(UiText::DynamicString),
                    createdAt = with(calculateDateBeforeUseCase(CalculateDateBeforeUseCase.Params(dto.createdDate))) {
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
                    },
                    isNew = !changeFromYnToBoolean(ChangeFromYnToBoolean.Params(dto.readYn)),
                    moreViewUrl = dto.moreViewUrl,
                  ),
                  uiEventModel = AlimUiModel.AlimUiEventModel(
                    noticeId = dto.noticeId,
                    contentsType = AlimContentsEnum.creator(dto.contentsType),
                  )
                )
              }
              is AlimListResponseDto.AlimDto.Trading -> {
                AlimUiModel(
                  uiBindingModel = AlimUiModel.AlimUiBindingModel(
                    emojiEnum = EmojiEnum.creator(dto.imogiCd),
                    name = UiText.DynamicString(dto.nickname),
                    contents = UiText.DynamicString(dto.contents),
                    createdAt = with(calculateDateBeforeUseCase(CalculateDateBeforeUseCase.Params(dto.createdDate))) {
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
                    },
                    isNew = !changeFromYnToBoolean(ChangeFromYnToBoolean.Params(dto.readYn)),
                  ),
                  uiEventModel = AlimUiModel.AlimUiEventModel(
                    contentsType = AlimContentsEnum.creator(dto.contentsType),
                    tradingId = dto.tradingId,
                  )
                )
              }
              is AlimListResponseDto.AlimDto.Activity -> {
                AlimUiModel(
                  uiBindingModel = AlimUiModel.AlimUiBindingModel(
                    emojiEnum = EmojiEnum.creator(dto.imogiCd),
                    name = UiText.DynamicString(dto.nickname),
                    contents = UiText.DynamicString(dto.contents),
                    subCommentContents = dto.subcommentContent?.let(UiText::DynamicString),
                    createdAt = with(calculateDateBeforeUseCase(CalculateDateBeforeUseCase.Params(dto.createdDate))) {
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
                    },
                    isNew = !changeFromYnToBoolean(ChangeFromYnToBoolean.Params(dto.readYn)),
                  ),
                  uiEventModel = AlimUiModel.AlimUiEventModel(
                    contentsType = AlimContentsEnum.creator(dto.contentsType),
                    tradingId = dto.tradingId,
                    buddyType = BuddyTypeEnum.creator(dto.buddyType),
                    buddyUserId = dto.buddyUserId,
                    buddyCnt = dto.buddyCnt,
                  )
                )
              }
            }
          }
        )
    }
  }
}

data class AlimCenterUiState(
  val receivingBuddyListUiModel: ReceivingBuddyListUiModel = ReceivingBuddyListUiModel(),
  val alimListUiModel: AlimListUiModel = AlimListUiModel(),
  val buddyDeleteCenterDialogUiModel: BuddyDeleteCenterDialogUiModel? = null,
  val isRefresh: Boolean = false
) : UiState {
  data class BuddyDeleteCenterDialogUiModel(
    val centerDialogUiModel: CenterDialogUiModel,
    val buddyId: Int,
    val buddyRequestId: Int
  )
}

sealed interface AlimCenterUiEvent : UiEvent {
  data class OnClickedReceivingBuddyItem(val uiModel: ReceivingBuddyListUiModel.ReceivingBuddyUiModel) : AlimCenterUiEvent
  data class OnClickedBuddyAccept(val uiModel: ReceivingBuddyListUiModel.ReceivingBuddyUiModel) : AlimCenterUiEvent
  data class OnClickedBuddyRejectInItem(val uiModel: ReceivingBuddyListUiModel.ReceivingBuddyUiModel) : AlimCenterUiEvent
  data class OnClickedBuddyRejectInDialog(val buddyId: Int, val buddyRequestId: Int) : AlimCenterUiEvent
  data class OnClickedBuddyBlockCheckBoxInRejectDialog(val isBlocked: Boolean) : AlimCenterUiEvent
  data class OnClickedAlimItem(val uiModel: AlimListUiModel.AlimUiModel) : AlimCenterUiEvent
  data object PulledToRefresh : AlimCenterUiEvent
  data object OnDismissDialog : AlimCenterUiEvent
}

sealed interface AlimCenterUiSideEffect : UiSideEffect {
  sealed interface Load : AlimCenterUiSideEffect {
    data object First : Load
    data object Refresh : Load
  }

  data class OnNavigatedTo(val uiModel: AlimListUiModel.AlimUiModel) : AlimCenterUiSideEffect
  data class OnNavigatedToReceivingRequestScreen(val uiModel: BaseBuddyInfoUiModel, val buddyRequestId: Int) : AlimCenterUiSideEffect
  data object OnNavigatedToBack : AlimCenterUiSideEffect
  data class OnShowToastMessage(val message: UiText) : AlimCenterUiSideEffect
}