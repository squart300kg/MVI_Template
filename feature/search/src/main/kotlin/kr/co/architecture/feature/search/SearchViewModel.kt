package kr.co.architecture.feature.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.architecture.core.domain.GetSortedImagesAndVideosByRecentlyUseCase
import kr.co.architecture.core.domain.ObserveBookmarkedMediasUseCase
import kr.co.architecture.core.domain.ToggleBookmarkUseCase
import kr.co.architecture.core.model.ContentsQuery
import kr.co.architecture.core.model.ToggleTypeEnum
import kr.co.architecture.core.model.uniqueId
import kr.co.architecture.core.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getSortedImagesAndVideosByRecentlyUseCase: GetSortedImagesAndVideosByRecentlyUseCase,
  private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
  private val observeBookmarkedMediasUseCase: ObserveBookmarkedMediasUseCase,
) : BaseViewModel<SearchUiState, SearchUiEvent, SearchUiSideEffect>() {

  private var query: String = ""
  private var pageNumber = 1
  private var bookmarkedKeys: Set<String> = emptySet()

  override fun createInitialState() = SearchUiState()

  override fun handleEvent(event: SearchUiEvent) {
    when (event) {
      is SearchUiEvent.OnScrolledToEnd -> {
        setEffect { SearchUiSideEffect.Load.More }
      }
      is SearchUiEvent.OnClickedBookmark -> {
        launchWithLoading {
          toggleBookmarkUseCase(
            params = ToggleBookmarkUseCase.Params(
              toggleType =
                if (event.item.isBookmarked) ToggleTypeEnum.DELETE
                else ToggleTypeEnum.SAVE,
              mediaContentsType = event.item.mediaContentsType,
              mediaContents = UiModelState.ContentsUiModel.mapperToDomainModel(event.item)
            )
          )
        }
      }
      is SearchUiEvent.OnQueryChange -> {
        query = event.query
      }
      is SearchUiEvent.OnSearch -> {
        setState { copy(uiModels = persistentListOf()) }
        setEffect { SearchUiSideEffect.Load.First }
      }
      is SearchUiEvent.OnClickedItem -> {

//        navigateTo(
//          route = DetailRoute(
//            id = event.item.id,
//            name = event.item.name.value ?: ""
//          )
//        )
      }
    }
  }

  init {
    observeBookmarkedMediasUseCase()
      .onEach { mediaContents ->
        // 1) 북마크 키 세트 생성
        val bookmarkedKeysLocal: Set<String> = mediaContents
          .asSequence()
          .map { it.uniqueId() }
          .toSet()

        // 2)  추후 fetchData 때 활용을 위한 메모리 캐싱
        bookmarkedKeys = bookmarkedKeysLocal

        // 3) uiState.uiModels에 북마크 반영
        setState {
          copy(
            uiModels = uiModels
              .map { uiModel ->
                when (uiModel) {
                  is UiModelState.ContentsUiModel -> {
                    val isBookmarked = bookmarkedKeysLocal.contains(uiModel.uniqueId())
                    uiModel.copy(isBookmarked = isBookmarked)
                  }
                  is UiModelState.PagingUiModel -> uiModel
                }
              }
              .toImmutableList()
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun fetchData(loadType: SearchUiSideEffect.Load) {
    launchWithLoading {
      if (loadType is SearchUiSideEffect.Load.First) {
        pageNumber = 1
      }

      val nextPage = when (loadType) {
        is SearchUiSideEffect.Load.First -> 1
        is SearchUiSideEffect.Load.More -> pageNumber + 1
      }
      val response = getSortedImagesAndVideosByRecentlyUseCase(
        query = ContentsQuery(
          query = query,
          page = nextPage
        )
      )
      setState {
        copy(
          uiType =
            if (loadType is SearchUiSideEffect.Load.First && response.mediaContentsList.isEmpty()) SearchUiType.EMPTY_RESULT
            else SearchUiType.LOADED_RESULT,
          uiModels = run {
            val pagingUiModel = UiModelState.PagingUiModel.mapperToUiModel(
              dto = response.pageableDto,
              page = nextPage
            )

            // 새 컨텐츠 -> UI 모델 변환
            val contentsUiModelsRaw = UiModelState.ContentsUiModel.mapperToUiModel(
              contents = response.mediaContentsList
            )

            // 캐시된 bookmarkedKeys 로 북마크 반영(한 번의 contains)
            val contentsUiModels = contentsUiModelsRaw
              .map { uiModel ->
                uiModel.copy(
                  isBookmarked = bookmarkedKeys.contains(uiModel.uniqueId())
                )
              }.toImmutableList()

            val uiModelState = (contentsUiModels as PersistentList<UiModelState>)
              .add(pagingUiModel)

            when (loadType) {
              is SearchUiSideEffect.Load.First -> uiModelState
              is SearchUiSideEffect.Load.More -> (uiState.value.uiModels as PersistentList)
                .addAll(uiModelState)
            }
          },
          isEndPage = response.pageableDto.isEnd
        )
      }.also { pageNumber = nextPage }
    }
  }
}