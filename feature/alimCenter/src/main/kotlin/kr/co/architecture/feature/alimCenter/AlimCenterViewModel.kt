package kr.co.architecture.feature.alimCenter

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.architecture.core.domain.CalculateDateBeforeUseCase
import kr.co.architecture.core.domain.ChangeFromYnToBoolean
import kr.co.architecture.core.domain.GetBuddyRequestAllItemUseCase
import kr.co.architecture.core.model.enums.AlimCenterTypeEnum
import kr.co.architecture.core.model.enums.BuddyRequestTypeEnum
import kr.co.architecture.core.repository.AlimRepository
import kr.co.architecture.core.repository.dto.AlimListRequestDto
import kr.co.architecture.core.ui.BaseViewModel
import kr.co.architecture.core.ui.CenterDialogUiModel
import kr.co.architecture.core.ui.model.ReceivingBuddyListUiModel
import kr.co.architecture.core.ui.util.UiText
import kr.co.architecture.feature.alimCenter.AlimCenterUiState.BuddyDeleteCenterDialogUiModel
import javax.inject.Inject
import kr.co.architecture.core.ui.R as coreUiR

private const val ALIM_CENTER_LIST_SIZE = 6

@HiltViewModel
class AlimCenterViewModel @Inject constructor(
  private val alimRepository: AlimRepository,
  private val getBuddyRequestAllItemUseCase: GetBuddyRequestAllItemUseCase,
  private val calculateDateBeforeUseCase: CalculateDateBeforeUseCase,
  private val changeFromYnToBoolean: ChangeFromYnToBoolean,
) : BaseViewModel<AlimCenterUiState, AlimCenterUiEvent, AlimCenterUiSideEffect>() {

  override fun createInitialState(): AlimCenterUiState {
    return AlimCenterUiState()
  }

  override fun handleEvent(event: AlimCenterUiEvent) {
    when (event) {
      is AlimCenterUiEvent.OnClickedReceivingBuddyItem -> {
        setEffect {
          AlimCenterUiSideEffect.OnNavigatedToReceivingRequestScreen(
            event.uiModel.baseBuddyInfoWithBuddyRequestUiModel.baseBuddyInfoUiModel,
            event.uiModel.buddyRequestId
          )
        }
      }
      is AlimCenterUiEvent.OnClickedBuddyAccept -> {
        setEffect { AlimCenterUiSideEffect.OnShowToastMessage(UiText.StringResource(coreUiR.string.buddy_request_accept)) }
      }
      is AlimCenterUiEvent.OnClickedBuddyRejectInItem -> {
        setState {
          copy(
            buddyDeleteCenterDialogUiModel = BuddyDeleteCenterDialogUiModel(
              centerDialogUiModel = CenterDialogUiModel(
                titleMessage = UiText.StringResource(coreUiR.string.buddy_delete_dialog_title),
                contentMessage = UiText.StringResource(coreUiR.string.buddy_delete_dialog_content),
                checkBoxVoState = CenterDialogUiModel.CheckBoxVo(
                  checkState = false,
                  checkMessage = UiText.StringResource(coreUiR.string.buddy_require_block)
                ),
                buttonStyleVo = CenterDialogUiModel.ButtonStyleVo.Type1(
                  leftMessage = UiText.StringResource(coreUiR.string.btn_cancel),
                  rightMessage = UiText.StringResource(coreUiR.string.buddy_edit_dialog_del)
                )
              ),
              buddyId = event.uiModel.baseBuddyInfoWithBuddyRequestUiModel.baseBuddyInfoUiModel.userId,
              buddyRequestId = event.uiModel.buddyRequestId,
            )
          )
        }

      }
      is AlimCenterUiEvent.OnClickedBuddyRejectInDialog -> {
        setEffect { AlimCenterUiSideEffect.OnShowToastMessage(UiText.StringResource(coreUiR.string.buddy_request_delete)) }
        setState { copy(buddyDeleteCenterDialogUiModel = null) }
      }
      is AlimCenterUiEvent.OnClickedBuddyBlockCheckBoxInRejectDialog -> {
        uiState.value.buddyDeleteCenterDialogUiModel?.let { state ->
          setState {
            copy(
              buddyDeleteCenterDialogUiModel = state.copy(
                centerDialogUiModel = state.centerDialogUiModel.copy(
                  checkBoxVoState = state.centerDialogUiModel.checkBoxVoState?.copy(
                    checkState = event.isBlocked
                  )
                )
              )
            )
          }
        }
      }
      is AlimCenterUiEvent.OnClickedAlimItem -> {
        setEffect { AlimCenterUiSideEffect.OnNavigatedTo(event.uiModel) }
      }
      is AlimCenterUiEvent.PulledToRefresh -> {

      }
      is AlimCenterUiEvent.OnDismissDialog -> {
        setState { copy(buddyDeleteCenterDialogUiModel = null) }
      }
    }
  }

  init { setEffect { AlimCenterUiSideEffect.Load.First } }

  fun fetchData(loadType: AlimCenterUiSideEffect.Load) {
    launchSafetyWithLoading(
      isPullToRefresh = loadType is AlimCenterUiSideEffect.Load.Refresh,
      block = {
        val buddyRequestListDto = getBuddyRequestAllItemUseCase(
          buddyRequestTypeEnum = BuddyRequestTypeEnum.RECEIVE
        )

        val alimListDto = alimRepository.getAlimList(
          dto = AlimListRequestDto(
            size = ALIM_CENTER_LIST_SIZE,
            page = 1,
            type = AlimCenterTypeEnum.ALL
          )
        )

        setState {
          copy(
            receivingBuddyListUiModel = ReceivingBuddyListUiModel.mapperToUiModel(
              dto = buddyRequestListDto,
              calculateDateBeforeUseCase = calculateDateBeforeUseCase
            ),
            alimListUiModel = uiState.value.alimListUiModel.copy(
              noticeAlimListUiModel = AlimListUiModel.mapperToUiModel(
                dtos = alimListDto.alimNoticeDto,
                calculateDateBeforeUseCase = calculateDateBeforeUseCase,
                changeFromYnToBoolean =  changeFromYnToBoolean
              ),
              salesHistoryAlimListUiModel = AlimListUiModel.mapperToUiModel(
                dtos = alimListDto.alimSalesHistoryDto,
                calculateDateBeforeUseCase = calculateDateBeforeUseCase,
                changeFromYnToBoolean =  changeFromYnToBoolean
              ),
              activityAlimListUiModel = AlimListUiModel.mapperToUiModel(
                dtos = alimListDto.alimActivityDto,
                calculateDateBeforeUseCase = calculateDateBeforeUseCase,
                changeFromYnToBoolean =  changeFromYnToBoolean
              ),
              moreNotice = alimListDto.moreNotice,
              moreTrading = alimListDto.moreTrading,
              moreActivity = alimListDto.moreActivity,
            )
          )
        }
      }
    )
  }
}