package kr.co.architecture.custom.image.loader.domain.mediator

import kr.co.architecture.custom.http.client.model.Bytes
import kr.co.architecture.custom.image.loader.domain.model.Meta

/**
 * 원본 바이트와 메타데이터를 보관하는 **디스크 캐시**.
 *
 * 동작/사용
 * - `getEntry(url)` → 미스/오류 시 `null`, 히트 시 바이트+메타 반환.
 * - HTTP 200 응답은 `putHttpResponse(...)`로 **본문+메타 저장**.
 * - HTTP 304 응답은 `updateMetaOn304(...)`로 **메타만 갱신**.
 * - 디스크 I/O를 수행하므로 호출 측에서 IO 컨텍스트에서 실행하세요.
 * - 헤더 키는 소문자-하이픈 형식을 기대합니다(예: `"cache-control"`, `"etag"`).
 */
interface ImageDiskCache {

  /**
   * 디스크에서 [url]에 해당하는 엔트리를 [DiskEntry]를 조회합니다.
   */
  fun getCachedEntry(url: String): DiskEntry?

  /**
   * HTTP **200(OK)** 응답 기반으로 [url]의 본문 [body]와 [header]를 저장합니다.
   * @param url 캐시 키로 사용할 절대 URL(파일 매핑 시 해시 사용 권장)
   * @param body HTTP 200 응답의 원본 바이트
   * @param header 요청/응답을 병합한 최종 헤더 맵(키는 소문자-하이픈)
   */
  fun cacheBodyAndMeta(url: String, body: ByteArray, header: Map<String, String>)

  /**
   * HTTP **304(Not Modified)** 응답을 반영하여 [url]의 **메타데이터만** 갱신합니다.
   * @param url 캐시 키로 사용할 절대 URL
   * @param header 304 응답 헤더(키는 소문자-하이픈)
   */
  fun cacheMeta(url: String, header: Map<String, String>)

  /**
   * 디스크 캐시 엔트리.
   *
   * @property bytes 저장된 원본 바이트(보통 HTTP 200 바디)
   * @property meta  저장 시점/만료/검증 토큰(ETag/Last-Modified)/캐시 정책 파생값을 담은 메타데이터
   */
  data class DiskEntry(val bytes: Bytes, val meta: Meta)
}
