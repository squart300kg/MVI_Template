package kr.co.architecture.feature.alimCenter

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.collections.immutable.ImmutableList
import kr.co.architecture.core.ui.BaseBuddyInfoWithBuddyRequestSection
import kr.co.architecture.core.ui.BaseCenterDialog
import kr.co.architecture.core.ui.GlobalUiStateEffect
import kr.co.architecture.core.ui.HtmlText
import kr.co.architecture.core.ui.baseClickable
import kr.co.architecture.core.ui.model.ReceivingBuddyListUiModel
import kr.co.architecture.core.ui.noRippledClickable
import kr.co.architecture.core.ui.util.asString
import kr.co.architecture.core.ui.R as coreUiR
import kr.co.architecture.core.ui.theme.*

const val ALIM_CENTER_BASE_ROUTE = "alimCenterBaseRoute"
fun NavGraphBuilder.secondScreen() {
  composable(
    route = ALIM_CENTER_BASE_ROUTE
  ) {
    AlimCenterScreen()
  }
}

@Composable
fun AlimCenterScreen(
  modifier: Modifier = Modifier,
  viewModel: AlimCenterViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  println("uiStateLog : $uiState")
  LaunchedEffect(Unit) {
//    viewModel.uiSideEffect.collect { effect ->
//      when (effect) {
//        is AlimCenterUiSideEffect.Load -> viewModel.fetchData()
//      }
//    }
  }
  AlimCenterScreen(
    uiState = uiState,
    modifier = modifier,
  )

  GlobalUiStateEffect(viewModel)
}

@Composable
fun AlimCenterScreen(
  modifier: Modifier = Modifier,
  uiState: AlimCenterUiState,
) {

//  when (uiState.uiType) {
//    AlimCenterUiType.NONE -> {}
//    AlimCenterUiType.LOADED -> {
//      LazyColumn(modifier) {
//        items(uiState.uiModels) { item ->
//          Text(
//            modifier = Modifier
//                .padding(8.dp)
//                .border(
//                    width = 1.dp,
//                    shape = RoundedCornerShape(4.dp),
//                    color = Color.LightGray
//                )
//                .padding(8.dp),
//            text = item.name.asString(),
//            style = TextStyle(
//              fontSize = 20.sp,
//            )
//          )
//        }
//      }
//    }
//  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlimCenterListContent(
  modifier: Modifier = Modifier,
  uiState: AlimCenterUiState,
  onNavigateToBuddyRequestScreen: () -> Unit = {},
  onClickedReceivedBuddyItem: (ReceivingBuddyListUiModel.ReceivingBuddyUiModel) -> Unit = { },
  onClickedAlimItem: (AlimListUiModel.AlimUiModel) -> Unit = {},
  onClickedBuddyAccept: (ReceivingBuddyListUiModel.ReceivingBuddyUiModel) -> Unit = {},
  onClickedBuddyRejectInItem: (ReceivingBuddyListUiModel.ReceivingBuddyUiModel) -> Unit = {},
  onClickedBuddyRejectInDialog: (buddyId: Int, buddyRequestId: Int) -> Unit = { _, _ -> },
  onClickedBuddyBlockCheckBoxInRejectDialog: (isBlocked: Boolean) -> Unit = { },
  onRefresh: () -> Unit = {},
  onDismissDialog: () -> Unit = {}
) {
  Column(modifier) {

    Box {
      val pullToRefreshState = rememberPullToRefreshState()
      LazyColumn(
        modifier = Modifier
          .pullToRefresh(
            state = pullToRefreshState,
            isRefreshing = uiState.isRefresh,
            onRefresh = onRefresh
          )
      ) {
        // 버디 요청 이동 헤더
        item {
          BuddyRequestHeaderContent(
            modifier = Modifier,
            onNavigateToBuddyRequestScreen = onNavigateToBuddyRequestScreen
          )
        }

        // 구분선
        item {
          HorizontalDivider(
            modifier = Modifier,
            color = colorResource(coreUiR.color.gray_DFE1E6),
            thickness = 1.dp
          )
        }

        // 요청받은 버디 리스트
        items(
          items = uiState.receivingBuddyListUiModel.receivingBuddyListUiModel
        ) { uiModel ->
          ReceivingBuddyRequestItem(
            modifier = Modifier
              .padding(
                start = 16.dp,
                end = 8.dp
              )
              .padding(vertical = 16.dp),
            receivingBuddyUiModel = uiModel,
            onClickedItem = { onClickedReceivedBuddyItem(uiModel) },
            onClickedAccept = {
              onClickedBuddyAccept(uiModel)
            },
            onClickedReject = { onClickedBuddyRejectInItem(uiModel) },
          )
        }

        // 구분선
        item {
          HorizontalDivider(
            modifier = Modifier,
            color = colorResource(coreUiR.color.gray_DFE1E6),
            thickness = 8.dp
          )
        }

        alimItems(
          ailmCategoryTitleRes = coreUiR.string.main_notice,
          alimListUiModel = uiState.alimListUiModel.noticeAlimListUiModel,
          isMoreView = uiState.alimListUiModel.moreNotice,
          onClickedAlimItem = onClickedAlimItem
        )

        // 구분선
        item {
          HorizontalDivider(
            modifier = Modifier,
            color = colorResource(coreUiR.color.gray_DFE1E6),
            thickness = 8.dp
          )
        }
        alimItems(
          ailmCategoryTitleRes = coreUiR.string.sales_history,
          alimListUiModel = uiState.alimListUiModel.salesHistoryAlimListUiModel,
          isMoreView = uiState.alimListUiModel.moreTrading,
          onClickedAlimItem = onClickedAlimItem
        )

        // 구분선
        item {
          HorizontalDivider(
            modifier = Modifier,
            color = colorResource(coreUiR.color.gray_DFE1E6),
            thickness = 8.dp
          )
        }

        alimItems(
          ailmCategoryTitleRes = coreUiR.string.alarm_active,
          alimListUiModel = uiState.alimListUiModel.activityAlimListUiModel,
          isMoreView = uiState.alimListUiModel.moreActivity,
          onClickedAlimItem = onClickedAlimItem
        )
      }

      uiState.buddyDeleteCenterDialogUiModel?.let { state ->
        BaseCenterDialog(
          uiModel = state.centerDialogUiModel.copy(
            checkBoxVoState = state.centerDialogUiModel.checkBoxVoState?.copy(
              onCheckStateChanged = onClickedBuddyBlockCheckBoxInRejectDialog
            )
          ),
          onClickedRightButton = { onClickedBuddyRejectInDialog(state.buddyId, state.buddyRequestId) },
          onClickedLeftButton = onDismissDialog,
          onDismissDialog = onDismissDialog,
        )
      }
    }
  }
}

private fun LazyListScope.alimItems(
  @StringRes ailmCategoryTitleRes: Int,
  alimListUiModel: ImmutableList<AlimListUiModel.AlimUiModel>,
  isMoreView: Boolean,
  onClickedAlimItem: (AlimListUiModel.AlimUiModel) -> Unit
) {
  item {
    Text(
      modifier = Modifier
        .padding(
          vertical = 16.dp,
          horizontal = 18.dp
        ),
      text = stringResource(ailmCategoryTitleRes),
      style = TypoSubhead2.copy(
        color = colorResource(id = coreUiR.color.gray_091E42),
        fontWeight = FontWeight.W500
      )
    )
  }

  when (alimListUiModel.isNotEmpty()) {
    true -> {
      itemsIndexed(
        items = alimListUiModel
      ) { index, alimVo ->
        AlimSection(
          modifier = Modifier,
          alimUiModel = alimVo,
          onClickedAlimItem = onClickedAlimItem
        )
      }

      if (isMoreView) {
        item {
          MoreViewSection(
            modifier = Modifier
              .padding(
                top = 16.dp,
                bottom = 24.dp
              )
              .padding(horizontal = 16.dp),
            onClickedMoreView = {}
          )
        }
      }
    }
    false -> {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
          Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(coreUiR.string.no_alim),
            style = TypoBody1.copy(
              fontWeight = FontWeight.W400,
              color = colorResource(coreUiR.color.gray_7A869A)
            )
          )
        }
      }
    }
  }
}

@Composable
private fun MoreViewSection(
  modifier: Modifier = Modifier,
  onClickedMoreView: () -> Unit = {}
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp)
      .background(
        shape = RoundedCornerShape(4.dp),
        color = colorResource(coreUiR.color.gray_C8CDD5)
      )
      .baseClickable(
        onClick = onClickedMoreView
      )
  ) {
    Text(
      modifier = Modifier
        .align(Alignment.Center),
      text = stringResource(coreUiR.string.more),
      style = TypoBody1.copy(
        color = colorResource(coreUiR.color.gray_091E42),
        fontWeight = FontWeight.W500
      )
    )
  }
}

@Composable
private fun AlimSection(
  modifier: Modifier = Modifier,
  alimUiModel: AlimListUiModel.AlimUiModel,
  onClickedAlimItem: (AlimListUiModel.AlimUiModel) -> Unit
) {
  Row(
    modifier = modifier
      .baseClickable { onClickedAlimItem(alimUiModel) }
      .padding(
        vertical = 12.dp,
        horizontal = 16.dp
      )
  ) {
    val emojiImageVectorState by rememberUpdatedState(
      when {
        alimUiModel.uiBindingModel.emojiEnum != null -> {
          alimUiModel.uiBindingModel.emojiEnum.imageVector
        }
        alimUiModel.uiBindingModel.buddyStockEmoji != null -> {
          alimUiModel.uiBindingModel.buddyStockEmoji
        }
        else -> null
      }
    )

    // 이모지
    emojiImageVectorState?.let { imageVector ->
      Image(
        modifier = Modifier
          .size(30.dp)
          .align(Alignment.Top),
        imageVector = imageVector,
        contentDescription = null
      )
    } ?: run {
      Box(
        modifier = Modifier
          .size(30.dp)
          .align(Alignment.Top)
      )
    }

    // 이름 & 알림 메인타이틀 & 서브타이틀
    Column(
      modifier = Modifier
        .padding(start = 10.dp)
        .weight(0.9f)
        .align(Alignment.CenterVertically)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = alimUiModel.uiBindingModel.name.asString(),
          style = TypoSubhead1.copy(
            color = colorResource(id = coreUiR.color.gray_091E42),
            fontWeight = FontWeight.W500
          )
        )
        Box {
          Text(
            modifier = Modifier
              .align(Alignment.Center),
            text = alimUiModel.uiBindingModel.createdAt.asString(),
            style = TypoCaption.copy(
              color = colorResource(id = coreUiR.color.gray_DFE1E6),
              fontWeight = FontWeight.W400
            )
          )

          if (alimUiModel.uiBindingModel.isNew) {
            Image(
              modifier = Modifier
                .size(5.dp)
                .padding(
                  start = 2.dp,
                  bottom = 2.dp,
                )
                .offset(
                  x = 2.dp,
                  y = -2.dp
                )
                .align(Alignment.TopEnd),
              painter = painterResource(id = coreUiR.drawable.ic_new_red_dot),
              contentDescription = null
            )
          }
        }
      }

      HtmlText(
        text = alimUiModel.uiBindingModel.contents.asString(),
//        text = "<font color=${colorHex(R.color.acua_green06)}>삼성전자</font>를 50,123원에 매수했어요.",
        style = TypoCaption.copy(
          color = colorResource(id = coreUiR.color.gray_091E42),
          fontWeight = FontWeight.W400
        )
      )

      alimUiModel.uiBindingModel.subCommentContents?.let { subCommentContents ->
        Text(
          modifier = Modifier
            .padding(top = 4.dp),
          text = alimUiModel.uiBindingModel.subCommentContents.asString(),
          style = TypoCaption.copy(
            color = colorResource(id = coreUiR.color.gray_7A869A),
            fontWeight = FontWeight.W400
          ),
          maxLines = 3,
          overflow = TextOverflow.Ellipsis
        )
      }

      alimUiModel.uiBindingModel.moreViewUrl?.let { moreViewUrl ->
        Box(
          modifier = modifier
            .padding(top = 4.dp)
            .fillMaxWidth()
            .height(32.dp)
            .border(
              width = 1.dp,
              color = colorResource(coreUiR.color.gray_C8CDD5),
              shape = RoundedCornerShape(8.dp)
            )
        ) {
          Text(
            modifier = Modifier
              .align(Alignment.Center),
            text = stringResource(coreUiR.string.view_detail),
            style = TypoCaption.copy(
              color = colorResource(coreUiR.color.gray_091E42),
              textAlign = TextAlign.Center,
              fontWeight = FontWeight.W400
            )
          )
        }
      }
    }
  }
}

@Composable
fun ReceivingBuddyRequestItem(
  modifier: Modifier = Modifier,
  receivingBuddyUiModel: ReceivingBuddyListUiModel.ReceivingBuddyUiModel,
  onClickedItem: () -> Unit = {},
  onClickedAccept: () -> Unit = {},
  onClickedReject: () -> Unit = {},
) {
  Row(modifier = modifier) {
    BaseBuddyInfoWithBuddyRequestSection(
      modifier = Modifier.weight(0.9f),
      emojiSize = 30.dp,
      baseBuddyInfoWithBuddyRequestUiModel = receivingBuddyUiModel.baseBuddyInfoWithBuddyRequestUiModel,
      onClickedBuddyItem = { onClickedItem() },
      onClickedBuddyRequest = { onClickedAccept() })

    // 버디 취소 엑스버튼
    Image(
      modifier = Modifier
        .padding(start = 4.dp)
        .align(Alignment.CenterVertically)
        .weight(0.1f)
        .noRippledClickable(onClick = onClickedReject),
      painter = painterResource(id = coreUiR.drawable.ic_close_black),
      contentDescription = null,
      colorFilter = ColorFilter.tint(colorResource(id = coreUiR.color.gray_97A0AF))
    )
  }
}

@Composable
private fun BuddyRequestHeaderContent(
  modifier: Modifier = Modifier,
  onNavigateToBuddyRequestScreen: () -> Unit = {}
) {
  Row(
    modifier = modifier
      .padding(vertical = 12.dp)
      .padding(
        start = 16.dp,
        end = 8.dp,
      )
      .noRippledClickable(onClick = onNavigateToBuddyRequestScreen)
  ) {
    Image(
      modifier = Modifier
        .size(30.dp)
        .align(Alignment.CenterVertically),
      painter = painterResource(id = coreUiR.drawable.ic_alim_center_buddy_request),
      contentDescription = null
    )

    Column(
      modifier = Modifier
        .padding(start = 10.dp)
        .align(Alignment.CenterVertically)
    ) {
      Text(
        text = stringResource(id = coreUiR.string.btn_buddy_apply),
        style =
          TypoBody1.copy(
          color = colorResource(id = coreUiR.color.gray_091E42),
          fontWeight = FontWeight.W500
        )
      )
      Text(
        text = stringResource(id = coreUiR.string.alarm_buddy_info),
        style = TypoCaption.copy(
          color = colorResource(id = coreUiR.color.gray_7A869A),
          fontWeight = FontWeight.W400
        )
      )
    }

    Spacer(modifier = Modifier.weight(1f))

    Image(
      modifier = Modifier.align(Alignment.CenterVertically),
      painter = painterResource(id = coreUiR.drawable.ic_arrow_right),
      contentDescription = null
    )
  }
}

@Preview(
  device = "spec:width=1080px,height=6000px,dpi=440"
)
@Composable
fun AlimCenterScreenPreview(
  @PreviewParameter(AlimCenterUiStatePreviewParam::class)
  alimCenterUiStatePreviewParam: AlimCenterUiState
) {
  BaseTheme {
    AlimCenterListContent(
      modifier = Modifier.fillMaxSize(),
      uiState = alimCenterUiStatePreviewParam
    )
  }
}