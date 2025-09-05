package kr.co.architecture.custom.image.loader.domain.mediator

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.Flow

/**
 * 이미지 로딩 상태.
 *
 * - [Loading]  : 로딩 중
 * - [Success]  : 성공(이미지 포함)
 * - [Failure]  : 실패(placeholder 등 표시)
 */
sealed interface ImageState {
  @JvmInline
  value class Success(val imageBitmap: ImageBitmap) : ImageState
  data object Loading : ImageState
  data object Failure : ImageState
}

/**
 * 메모리/디스크/네트워크를 조합해 이미지를 제공하는 **중재자**.
 *
 * 동작
 * - 반환값은 **cold Flow** 입니다(수집 시 시작).
 * - SWR이 유효하면 **stale → fresh** 순으로 최대 2회 [ImageState.Success]가 나올 수 있습니다.
 * - 오류 시 `stale-if-error`가 허용되면 stale로 [Success], 아니면 [Failure].
 *
 * 방출 규칙
 * - 메모리 히트          → `Loading` 생략 가능, `Success(메모리)`
 * - 디스크 fresh         → `Success(디스크)`
 * - 디스크 stale+SWR     → `Success(stale)` 후 재검증 성공 시 `Success(fresh)`
 * - 네트워크 200         → `Success(네트워크)`
 * - 네트워크 304         → 메타 갱신 후 `Success(디스크)`
 * - 재검증 실패/네트워크 오류 → `Failure` (단, SiE면 stale로 `Success`)
 *
 * 사용 예:
 * ```
 * val state by mediator.imageFlow(url).collectAsState(ImageState.Loading)
 * ```
 */
interface ImageMediator {

  /** [url]의 이미지를 비동기로 로드해 [ImageState]를 방출합니다. */
  fun imageFlow(url: String): Flow<ImageState>
}