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
import kr.co.architecture.core.ui.model.ReceivingBuddyListUiModel
import javax.inject.Inject

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
      else -> {}
    }
  }

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