package kr.co.architecture.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.architecture.core.domain.ObserveBookmarkedMediasUseCase
import kr.co.architecture.core.domain.SearchMediaContentsUseCase
import kr.co.architecture.core.domain.ToggleBookmarkUseCase
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.model.uniqueId
import kr.co.architecture.core.router.AppDeepLinks
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ID
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ORIGIN
import kr.co.architecture.core.ui.BaseViewModel
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
  private val searchMediaContentsUseCase: SearchMediaContentsUseCase,
  private val observeBookmarkedMediasUseCase: ObserveBookmarkedMediasUseCase
) : BaseViewModel<DetailUiState, DetailUiEvent, DetailUiSideEffect>() {

  override fun createInitialState() = DetailUiState()

  override fun handleEvent(event: DetailUiEvent) {
    when (event) {
      is DetailUiEvent.OnClickedBookmark -> {
        // TODO: 좋아요 클릭할때 리스트 화면 전체 깜빡임버그
        // TODO: globalUiBus연동
        println("bookmarkLog 0; ${event.uiModel}")

        launchWithLoading {
          val toggleType =
            if (event.uiModel.bindingUiModel.isBookmarked) ToggleTypeEnum.DELETE
            else ToggleTypeEnum.SAVE

          toggleBookmarkUseCase(
            ToggleBookmarkUseCase.Params(
              toggleType = toggleType,
              mediaContents = UiModel.mapperToDomain(event.uiModel)
            )
          )

          setState {
            val index = uiModels.indexOfFirst { it.uniqueId() == event.uiModel.uniqueId() }
            require(index >= 0) { "index should not be negative." }

            val uiModels = LinkedList(this.uiModels)
            uiModels[index] = uiModels[index].copy(
              bindingUiModel = uiModels[index].bindingUiModel.copy(
                isBookmarked = toggleType == ToggleTypeEnum.SAVE
              )
            ).also {
              println("bookmarkLog 1; $it")
            }
            copy(uiModels = uiModels.also {
              println("bookmarkLog 2; ${it[index]}")
            })
          }
        }
      }

      is DetailUiEvent.OnClickedBack -> {
        setEffect { DetailUiSideEffect.OnFinish }
      }
      is DetailUiEvent.OnSwipe -> {
        setState { copy(startIndex = event.position) }
      }
    }
  }

  init {
    runCatching {
      val id = requireNotNull(savedStateHandle.get<String?>(ID)) {
        "ID cannot be null."
      }
      val origin = requireNotNull(savedStateHandle.get<String?>(ORIGIN)) {
        "ORIGIN cannot be null."
      }
      // TODO: usecase로 옮길수있을지?
      viewModelScope.launch {
        when (AppDeepLinks.Detail.Origin.valueOf(origin)) {
          AppDeepLinks.Detail.Origin.SEARCH -> {
            // 1개짜리 원소 리스트 구축 (스크롤 없음)
            searchMediaContentsUseCase(id)?.let { mediaContents ->
              setState {
                DetailUiState(
                  uiType = DetailUiType.LOADED,
                  uiModels = LinkedList<UiModel>().apply {
                    add(
                      UiModel(
                        bindingUiModel = UiModel.BindingUiModel(
                          title = mediaContents.title,
                          thumbnailUrl = mediaContents.thumbnailUrl,
                          isBookmarked = true,
                        ),
                        unbindingUiModel = UiModel.UnbindingUiModel(
                          dateTime = mediaContents.dateTime,
                          collection = mediaContents.collection,
                          contents = mediaContents.contents,
                          mediaContentsType = mediaContents.mediaContentsType,
                        )
                      )
                    )
                  },
                  startIndex = 0
                )
              }
            }
          }
          AppDeepLinks.Detail.Origin.BOOKMARK -> {
            // N개짜리 리스트 구축, 시작 인덱스 산출 (스크롤 있음)
            val mediaContentsList = observeBookmarkedMediasUseCase().first()
            val initial = LinkedList<UiModel>() to -1
            val (uiModels, startIndex) = mediaContentsList.foldIndexed(initial) { index, acc, mediaContents ->
              val (uiModels, searchedIndex) = acc
              uiModels.add(
                UiModel(
                  bindingUiModel = UiModel.BindingUiModel(
                    title = mediaContents.title,
                    thumbnailUrl = mediaContents.thumbnailUrl,
                    isBookmarked = true,
                  ),
                  unbindingUiModel = UiModel.UnbindingUiModel(
                    dateTime = mediaContents.dateTime,
                    collection = mediaContents.collection,
                    contents = mediaContents.contents,
                    mediaContentsType = mediaContents.mediaContentsType,
                  )
                )
              )
              val newlyFoundIndex =
                if (searchedIndex == -1 && mediaContents.uniqueId() == id) index
                else searchedIndex
              uiModels to newlyFoundIndex
            }

            setState {
              DetailUiState(
                uiType = DetailUiType.LOADED,
                uiModels = uiModels,
                startIndex = startIndex
              )
            }
          }
        }
      }
    }.onFailure { setEffect { DetailUiSideEffect.OnFinish } }
  }
}