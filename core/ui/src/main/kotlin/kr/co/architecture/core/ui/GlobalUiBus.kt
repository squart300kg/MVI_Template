package kr.co.architecture.core.ui

import kotlinx.coroutines.flow.StateFlow

/**
 * 전역 UI 상태(로딩, 에러 다이얼로그)를 송출하는 버스(Bus) 인터페이스.
 *
 * - 화면 어디서든 로딩/에러를 일관되게 노출하기 위한 상위 계층 채널
 * - ViewModel 등에서 호출하고, Activity/최상위 Composable에서 구독하여 UI로 그립니다.
 *
 * 수집 팁:
 * ```
 * val isLoading by globalUiBus.loadingState.collectAsStateWithLifecycle()
 * val dialog    by globalUiBus.errorDialog.collectAsStateWithLifecycle()
 * ```
 */
interface GlobalUiBus {

  /**
   * 전역 로딩 여부 스트림.
   *
   * 구현체는 내부적으로 카운팅 방식(중첩 로딩 허용)을 사용해
   * 0 초과일 때만 `true`를 방출합니다.
   */
  val loadingState: StateFlow<Boolean>

  /**
   * 전역 에러 다이얼로그 모델 스트림.
   * - `null`이면 다이얼로그를 닫습니다.
   * - 값이 non-null이면 해당 모델로 다이얼로그를 표시합니다.
   */
  val failureDialog: StateFlow<BaseCenterDialogUiModel?>

  /**
   * 전역 로딩 상태를 토글합니다.
   * - `true` → 로딩 카운트 +1
   * - `false` → 로딩 카운트 -1 (최소 0)
   *
   * @param loadingState 추가/감소 여부
   */
  fun setLoadingState(loadingState: Boolean)

  /**
   * 전역 에러 다이얼로그를 표시합니다.
   * 구현체는 전달된 예외를 화면 친화적 메시지로 매핑합니다.
   *
   * @param throwable 표시할 실패 원인
   */
  fun showFailureDialog(throwable: Throwable)

  /**
   * 현재 표시 중인 전역 에러 다이얼로그를 닫습니다.
   */
  fun dismissDialog()
}