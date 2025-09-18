package kr.co.architecture.feature.search

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kr.co.architecture.core.model.MediaContentsTypeEnum
import kr.co.architecture.core.model.MediaContents
import kr.co.architecture.core.model.MediaIdentity
import kr.co.architecture.core.repository.dto.PageableDto
import kr.co.architecture.core.ui.UiEvent
import kr.co.architecture.core.ui.UiSideEffect
import kr.co.architecture.core.ui.UiState
import kr.co.architecture.core.ui.util.UiText

enum class SearchUiType {
  NONE,
  LOADED_RESULT,
  EMPTY_RESULT
}

@Immutable
sealed interface UiModelState {
  data class ContentsUiModel(
    val thumbnailUrl: String,
    val dateTime: String,
    val title: String,
    val collection: String? = null,
    val contents: String,
    val mediaContentsType: MediaContentsTypeEnum,
    val isBookmarked: Boolean = false
  ): UiModelState {
    companion object {
      fun mapperToUiModel(contents: List<MediaContents>) =
        contents.map {
          ContentsUiModel(
            thumbnailUrl = it.thumbnailUrl,
            dateTime = it.dateTime,
            title = it.title,
            collection = it.collection,
            contents = it.contents,
            mediaContentsType = when (it.collection != null) {
              true -> MediaContentsTypeEnum.IMAGE
              false -> MediaContentsTypeEnum.VIDEO
            }
          )
        }.toImmutableList()

      fun mapperToDomainModel(uiModels: ContentsUiModel) =
        MediaContents(
          thumbnailUrl = uiModels.thumbnailUrl,
          dateTime = uiModels.dateTime,
          title = uiModels.title,
          collection = uiModels.collection,
          contents = uiModels.contents,
          mediaContentsType = uiModels.mediaContentsType,
        )
    }
  }

  data class PagingUiModel(
    val page: UiText
  ): UiModelState {
    companion object {
      fun mapperToUiModel(
        dto: PageableDto,
        page: Int
      ) =
        PagingUiModel(
          page = when (dto.isEnd) {
            false -> UiText.DynamicString("$page")
            true -> UiText.StringResource(R.string.last)
          }
        )
    }
  }
}


fun UiModelState.ContentsUiModel.uniqueId(): String =
  MediaIdentity.idOf(mediaContentsType, title, contents, thumbnailUrl)

data class SearchUiState(
  val uiType: SearchUiType = SearchUiType.NONE,
  val uiModels: ImmutableList<UiModelState> = persistentListOf(),
  val isEndPage: Boolean = true
) : UiState

sealed interface SearchUiEvent : UiEvent {
  data class OnClickedItem(val item: UiModelState.ContentsUiModel) : SearchUiEvent
  data object OnScrolledToEnd : SearchUiEvent
  data class OnClickedBookmark(val item: UiModelState.ContentsUiModel) : SearchUiEvent
  data class OnQueryChange(val query: String) : SearchUiEvent
  data object OnSearch : SearchUiEvent
}

sealed interface SearchUiSideEffect : UiSideEffect {
  sealed interface Load : SearchUiSideEffect {
    data object First: Load
    data object More: Load
  }
}
