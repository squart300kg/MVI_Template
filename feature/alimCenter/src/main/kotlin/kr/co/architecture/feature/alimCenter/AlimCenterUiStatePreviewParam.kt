package kr.co.architecture.feature.alimCenter

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.toPersistentList
import kr.co.architecture.core.model.enums.AlimContentsEnum
import kr.co.architecture.core.model.enums.BuddyStatus
import kr.co.architecture.core.ui.BaseBuddyInfoUiModel
import kr.co.architecture.core.ui.BaseBuddyInfoWithBuddyRequestUiModel
import kr.co.architecture.core.ui.enums.BuddyTypeEnum
import kr.co.architecture.core.ui.enums.EmojiEnum
import kr.co.architecture.core.ui.imageVector.emojis.IcEmojiBuddyStockLogo
import kr.co.architecture.core.ui.model.ReceivingBuddyListUiModel
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.feature.alimCenter.AlimListUiModel.AlimUiModel

class AlimCenterUiStatePreviewParam : PreviewParameterProvider<AlimCenterUiState> {
  override val values: Sequence<AlimCenterUiState> = sequenceOf(
    AlimCenterUiState(
      receivingBuddyListUiModel = ReceivingBuddyListUiModel(
        receivingBuddyListUiModel = mutableListOf<ReceivingBuddyListUiModel.ReceivingBuddyUiModel>()
          .apply {
            List(3) {
              add(
                ReceivingBuddyListUiModel.ReceivingBuddyUiModel(
                  baseBuddyInfoWithBuddyRequestUiModel = BaseBuddyInfoWithBuddyRequestUiModel(
                    baseBuddyInfoUiModel = BaseBuddyInfoUiModel(
                      userId = it,
                      emojiEnum = EmojiEnum.entries[it],
                      titleText = UiText.DynamicString("SSY_$it"),
                      buddyType =
                        if (it % 2 == 0) BuddyTypeEnum.NICKNAME
                        else BuddyTypeEnum.CONTACT,
                      contentText = UiText.DynamicString("버디 99명")
                    ),
                    buddyStatus = BuddyStatus.WAITING_MY_ACCEPT,
                  ),
                  buddyRequestId = it
                )
              )
            }
          }
          .toPersistentList()),
      alimListUiModel = AlimListUiModel(
        noticeAlimListUiModel = mutableListOf<AlimUiModel>()
          .apply {
            add(
              AlimUiModel(
                uiBindingModel = AlimUiModel.AlimUiBindingModel(
                  buddyStockEmoji = IcEmojiBuddyStockLogo,
                  subCommentContents = UiText.DynamicString("공지사항 상세정보"),
                  moreViewUrl = "https://www.naver.com",
                  name = UiText.DynamicString("버디스탁"),
                  contents = UiText.DynamicString("공지사항"),
                  createdAt = UiText.DynamicString(""),
                  isNew = true,
                ),
                uiEventModel = AlimUiModel.AlimUiEventModel(
                  contentsType = AlimContentsEnum.NONE
                )
              )
            )
            add(
              AlimUiModel(
                uiBindingModel = AlimUiModel.AlimUiBindingModel(
                  buddyStockEmoji = IcEmojiBuddyStockLogo,
                  subCommentContents = UiText.DynamicString("버디스탁 업데이트 안내"),
                  moreViewUrl = "https://www.naver.com",
                  name = UiText.DynamicString("버디스탁"),
                  contents = UiText.DynamicString("공지사항"),
                  createdAt = UiText.DynamicString(""),
                  isNew = true,
                ),
                uiEventModel = AlimUiModel.AlimUiEventModel(
                  contentsType = AlimContentsEnum.NONE
                )
              )
            )
            add(
              AlimUiModel(
                uiBindingModel = AlimUiModel.AlimUiBindingModel(
                  buddyStockEmoji = IcEmojiBuddyStockLogo,
                  subCommentContents = UiText.DynamicString("개인정보 처리 방침이 변경되었어요."),
                  moreViewUrl = "https://www.naver.com",
                  name = UiText.DynamicString("버디스탁"),
                  contents = UiText.DynamicString("개인정보 처리방침 변경"),
                  createdAt = UiText.DynamicString(""),
                  isNew = true,
                ),
                uiEventModel = AlimUiModel.AlimUiEventModel(
                  contentsType = AlimContentsEnum.NONE
                )
              )
            )

          }
          .toPersistentList(),
        salesHistoryAlimListUiModel = mutableListOf<AlimUiModel>()
          .apply {
            List(6) {
              add(
                AlimUiModel(
                  uiBindingModel = AlimUiModel.AlimUiBindingModel(
                    emojiEnum = EmojiEnum.entries[it],
                    buddyStockEmoji = IcEmojiBuddyStockLogo,
                    subCommentContents = UiText.DynamicString(""),
                    name = UiText.DynamicString("SSY_$it"),
                    contents = UiText.DynamicString("삼성전자를 50,000원에 매도했어요."),
                    createdAt = UiText.DynamicString("$it 시간전"),
                    isNew = it < 3,
                  ),
                  uiEventModel = AlimUiModel.AlimUiEventModel(
                    contentsType = AlimContentsEnum.NONE
                  )
                )
              )
            }
          }
          .toPersistentList(),
        activityAlimListUiModel = mutableListOf<AlimUiModel>()
          .apply {
            List(6) {
              add(
                AlimUiModel(
                  uiBindingModel = AlimUiModel.AlimUiBindingModel(
                    emojiEnum = EmojiEnum.entries[it],
                    buddyStockEmoji = IcEmojiBuddyStockLogo,
                    subCommentContents = UiText.DynamicString(if (it % 2 == 0) "매매노트 블라블라~ 매매노트 블라블라~ 매매노트 블라블라~ 매매노트 블라블라~" else "댓글 블라블라~ 댓글 블라블라~ 댓글 블라블라~ 댓글 블라블라~"),
                    name = UiText.DynamicString("SSY_$it"),
                    contents = UiText.DynamicString(if (it % 2 == 0) "삼성전자 내역에 매매노트가 작성되었어요." else "삼성전자 내역에 댓글이 작성되었어요."),
                    createdAt = UiText.DynamicString("$it 시간전"),
                    isNew = it < 3
                  ),
                  uiEventModel = AlimUiModel.AlimUiEventModel(
                    contentsType = AlimContentsEnum.NONE
                  )
                )
              )
            }
          }
          .toPersistentList(),
        moreTrading = true,
        moreActivity = true
      )
    )
  )
}