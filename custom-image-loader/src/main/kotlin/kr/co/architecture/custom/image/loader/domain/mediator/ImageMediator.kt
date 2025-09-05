package kr.co.architecture.custom.image.loader.domain.mediator

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.Flow

/**
 * 이미지 로딩을 오케스트레이션하는 중재자(Mediator).
 *
 * 역할
 * - 성공한 로딩 결과를 메모리/디스크 캐시에 기록합니다.
 * - 메모리/디스크/네트워크 소스를 순서대로 확인하고, 정책(SWR, stale-if-error 등)에 따라 이미지를 흘려보냅니다.
 * - ETag/Last-Modified 기반 조건부 요청과 메타데이터 갱신을 처리할 수 있습니다.
 *
 * 동작 특성
 * - 반환하는 Flow는 **cold** 입니다. 수집할 때마다 로딩이 수행됩니다.
 * - SWR(stale-while-revalidate)이 활성인 경우, **최대 2회** 방출될 수 있습니다(예: stale → fresh).
 *
 * 예시
 * ```
 * val bitmap by imageMediator.imageFlow(url).collectAsState(initial = null)
 * // bitmap != null 이면 그려주고, null이면 placeholder 표시
 * ```
 */
interface ImageMediator {

  /**
   * 주어진 [url]의 이미지를 비동기로 로딩해 방출하는 **cold** [Flow].
   *
   * 방출 규칙
   * - 메모리 캐시 히트: 즉시 1회 [ImageBitmap] 방출 후 완료.
   * - 디스크 캐시 **fresh**: 디코드 후 1회 방출 후 완료.
   * - 디스크 캐시 **stale + SWR 윈도우**: stale을 즉시 1회 방출 → 재검증 성공 시 fresh를 **추가 1회** 방출.
   *   - 재검증 304(Not Modified) 또는 실패 시 추가 방출 없음(= stale 1장으로 종료).
   * - 캐시 미스 + 네트워크 200: 본문 디코드 후 1회 방출.
   * - 네트워크 304: 메타만 갱신 후 캐시 본문 디코드하여 1회 방출.
   * - 오류: `stale-if-error` 허용이면 stale 1회 방출, 아니면 `null` 방출(placeholder 용).
   *
   * 반환값
   * - 성공 시 [ImageBitmap], 표시 불가/디코드 실패/정책상 미표시 시 `null`.
   * - 상황에 따라 **0~2회** 방출될 수 있습니다.
   *
   * 주의
   * - Flow가 **cold** 이므로 동일 URL을 여러 곳에서 동시에 수집하면 각자 로딩합니다.
   *   공유/중복 방지를 원하면 호출측에서 `shareIn(scope, …, replay = 1)` 등을 적용하세요.
   *
   * @param url 로딩할 원본 이미지의 절대 URL.
   * @return [ImageBitmap] 또는 `null`을 0~2회 방출하는 [Flow].
   */
  // TODO: doc수정
  fun imageFlow(url: String): Flow<ImageState>
}