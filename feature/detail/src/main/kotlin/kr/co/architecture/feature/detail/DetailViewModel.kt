package kr.co.architecture.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kr.co.architecture.core.domain.ObserveBookmarkedMediasUseCase
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.uniqueId
import kr.co.architecture.core.router.AppDeepLinks
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ID
import kr.co.architecture.core.router.AppDeepLinks.Detail.ArgsKey.ORIGIN
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val observeBookmarkedMediasUseCase: ObserveBookmarkedMediasUseCase
) : BaseViewModel<DetailUiState, DetailUiEvent, DetailUiSideEffect>() {

  override fun createInitialState() = DetailUiState()

  override fun handleEvent(event: DetailUiEvent) {
    when (event) {
      is DetailUiEvent.OnClickedBookmark -> {

      }
      is DetailUiEvent.OnClickedBack -> {
        setEffect { DetailUiSideEffect.OnFinish }
      }
      is DetailUiEvent.OnSwipe -> {
        setState {
          copy(startIndex = event.position)
        }
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
      println("detailLog 2; $id, $origin")

      observeBookmarkedMediasUseCase()
        .map { mediaContentsList ->
          when (AppDeepLinks.Detail.Origin.valueOf(origin)) {
            AppDeepLinks.Detail.Origin.BOOKMARK -> {
              // N개짜리 리스트 구축, 시작 인덱스 산출 (스크롤 있음)
              val initial = ArrayList<MediaContents>(mediaContentsList.size) to -1
              val (pages, startIndex) = mediaContentsList.foldIndexed(initial) { index, acc, mediaContents ->
                val (mediaContentsList, searchedIndex) = acc
                mediaContentsList.add(mediaContents)
                val newlyFoundIndex =
                  if (searchedIndex == -1 && mediaContents.uniqueId() == id) index
                  else searchedIndex
                mediaContentsList to newlyFoundIndex
              }
              setState {
                DetailUiState(
                  uiType = DetailUiType.LOADED,
                  pages = pages,
                  startIndex = startIndex
                )
              }
            }

            AppDeepLinks.Detail.Origin.SEARCH -> {
              // 1개짜리 원소 리스트 구축 (스크롤 없음)
              mediaContentsList
                .firstOrNull { it.uniqueId() == id }
                ?.let { target ->
                  setState {
                    DetailUiState(
                      uiType = DetailUiType.LOADED,
                      pages = listOf(target),
                      startIndex = 0
                    )
                  }
                }
            }
          }
        }
        .distinctUntilChanged()
        .launchIn(viewModelScope)
    }.onFailure { setEffect { DetailUiSideEffect.OnFinish } }

  }
}