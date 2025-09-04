package kr.co.architecture.custom.image.loader.domain.mediator

import kr.co.architecture.custom.image.loader.domain.model.Meta

/**
 * 영속(디스크) 이미지 캐시 인터페이스.
 *
 * 개요
 * - URL을 키로 **원본 바이트(body)** 와 **메타데이터([Meta])** 를 저장/조회합니다.
 * - 200 응답은 본문+메타를 저장하고, 304 응답은 **메타만** 갱신합니다.
 * - LRU 기반의 용량 관리(예: 64MB)와 **원자적 쓰기**(tmp → rename)를 권장합니다.
 *
 * 계약(Contract)
 * - **스레드 안전**해야 합니다. (다중 코루틴/스레드 접근 가능)
 * - 메서드는 **블로킹 디스크 I/O**를 수행할 수 있으므로 호출 측에서 `Dispatchers.IO`에서 실행하세요.
 * - `header`의 키는 **소문자-하이픈** 형식으로 정규화되어 있다고 가정합니다
 *   (예: `"cache-control"`, `"etag"`, `"last-modified"`, `"age"`).
 * - `Cache-Control: no-store` 응답은 저장하지 않습니다.
 * - `max-age`/`immutable`/`age` 등을 활용해 만료 시각을 계산하고,
 *   `stale-while-revalidate`/`stale-if-error` 값도 메타에 반영합니다.
 *
 * 사용 시나리오(일반적 구현 기준)
 * 1) 네트워크 200:
 *    - `putHttpResponse(url, body, header)` 호출 → 본문과 메타 저장
 * 2) 네트워크 304:
 *    - `updateMetaOn304(url, header)` 호출 → 메타만 갱신(본문 유지)
 * 3) 조회:
 *    - `getEntry(url)` → `null`(미스/손상) 또는 [DiskEntry] 반환
 *
 * 에러 처리 권장
 * - `getEntry`는 예외 대신 `null`을 반환(실패-소거)하도록 합니다.
 * - `putHttpResponse`/`updateMetaOn304`는 구현에 따라 내부 처리/로그 후 무시 또는 예외 전파를 일관되게 택하세요.
 *
 * 예시
 * ```
 * // 200 응답 처리
 * imageDiskCache.putHttpResponse(url, body, mergedHeader)
 *
 * // 304 응답 처리
 * imageDiskCache.updateMetaOn304(url, responseHeader)
 *
 * // 조회
 * val entry = imageDiskCache.getEntry(url)
 * if (entry != null && entry.meta.isFresh()) {
 *   // entry.bytes를 디코드하여 사용
 * }
 * ```
 */
interface ImageDiskCache {

  /**
   * 디스크에서 [url]에 해당하는 엔트리를 조회합니다.
   *
   * 구현 가이드:
   * - 데이터 파일과 메타 파일이 모두 존재하고 파싱 가능할 때 [DiskEntry]를 반환합니다.
   * - LRU 최신화를 위해 파일의 `lastModified`를 touch(접근 시각 갱신)하는 것을 권장합니다.
   * - 파일 누락/손상/파싱 실패 등 오류는 **예외 대신** `null`을 반환하세요.
   */
  fun getEntry(url: String): DiskEntry?

  /**
   * HTTP **200(OK)** 응답 기반으로 [url]의 본문 [body]와 [header]를 저장합니다.
   *
   * 구현 가이드:
   * - `Cache-Control: no-store`면 저장하지 않습니다.
   * - `Cache-Control(max-age, immutable)` + `Age`로 만료 시각을 계산해 [Meta.expiresAtMillis]에 기록합니다.
   * - `ETag`/`Last-Modified`를 메타에 기록하고, `stale-while-revalidate`/`stale-if-error` 값도 반영합니다.
   * - 부분 손상 방지를 위해 **원자적 쓰기**(tmp → rename)를 사용하고, 저장 후 필요 시 **eviction**을 수행하세요.
   *
   * @param url 캐시 키로 사용할 절대 URL(파일 매핑 시 해시 사용 권장)
   * @param body HTTP 200 응답의 원본 바이트
   * @param header 요청/응답을 병합한 최종 헤더 맵(키는 소문자-하이픈)
   */
  fun putHttpResponse(url: String, body: ByteArray, header: Map<String, String>)

  /**
   * HTTP **304(Not Modified)** 응답을 반영하여 [url]의 **메타데이터만** 갱신합니다.
   *
   * 구현 가이드:
   * - 본문 파일은 변경하지 않고, 304 응답의 `Cache-Control`/`Age`로 만료 시각을 재계산해 메타만 업데이트합니다.
   * - 304에도 새로운 `Cache-Control`이 올 수 있으므로 있으면 **새 정책을 우선 적용**하세요.
   * - LRU 최신화를 위해 데이터/메타 파일의 `lastModified`를 touch하는 것을 권장합니다.
   *
   * @param url 캐시 키로 사용할 절대 URL
   * @param header 304 응답 헤더(키는 소문자-하이픈)
   */
  fun updateMetaOn304(url: String, header: Map<String, String>)

  /**
   * 디스크 캐시 엔트리.
   *
   * @property bytes 저장된 원본 바이트(보통 HTTP 200 바디)
   * @property meta  저장 시점/만료/검증 토큰(ETag/Last-Modified)/캐시 정책 파생값을 담은 메타데이터
   */
  data class DiskEntry(val bytes: ByteArray, val meta: Meta)
}
