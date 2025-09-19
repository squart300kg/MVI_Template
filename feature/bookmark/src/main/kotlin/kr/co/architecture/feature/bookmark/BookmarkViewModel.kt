package kr.co.architecture.feature.bookmark

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.architecture.core.domain.ObserveBookmarkedMediasUseCase
import kr.co.architecture.core.domain.ToggleBookmarkUseCase
import kr.co.architecture.core.domain.formatter.EraseDateUnderDayFormatter
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.router.AppDeepLinks
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
  private val observeBookmarkedMediasUseCase: ObserveBookmarkedMediasUseCase,
  private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
  private val eraseDateUnderDayFormatter: EraseDateUnderDayFormatter
) : BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkUiSideEffect>() {

  override fun createInitialState() = BookmarkUiState()

  override fun handleEvent(event: BookmarkUiEvent) {
    when (event) {
      is BookmarkUiEvent.OnClickedItem -> {
        navigateDeepLink(
          url = AppDeepLinks.Detail.build(
            args = AppDeepLinks.Detail.Args(
              id = event.uiModelState.uniqueId(),
              origin = AppDeepLinks.Detail.Origin.BOOKMARK
            )
          ),
          extras = mapOf(
            AppDeepLinks.Detail.ArgsKey.ID to event.uiModelState.uniqueId(),
            AppDeepLinks.Detail.ArgsKey.ORIGIN to AppDeepLinks.Detail.Origin.BOOKMARK.name
          )
        )
      }
      is BookmarkUiEvent.OnClickedBookmark -> {
        launchWithLoading {
          toggleBookmarkUseCase(
            params = ToggleBookmarkUseCase.Params(
              toggleType =
                if (event.uiModelState.bindingUiModel.isBookmarked) ToggleTypeEnum.DELETE
                else ToggleTypeEnum.SAVE,
              mediaContentsType = event.uiModelState.bindingUiModel.mediaContentsType,
              mediaContents = UiModel.mapperToDomainModel(event.uiModelState)
            )
          )
        }
      }
    }
  }

  init {
    observeBookmarkedMediasUseCase()
      .onEach { mediaContents ->
        setState {
          copy(
            uiType =
              if (mediaContents.isNotEmpty()) BookmarkUiType.LOADED_RESULT
              else BookmarkUiType.EMPTY_RESULT,
            uiModels = UiModel.mapperToUiModel(
              contents = mediaContents,
              eraseDateUnderDayFormatter = eraseDateUnderDayFormatter
            )
          )
        }
      }.launchIn(viewModelScope)
  }
}