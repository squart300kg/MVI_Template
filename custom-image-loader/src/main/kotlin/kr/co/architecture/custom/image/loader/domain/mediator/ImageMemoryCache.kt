package kr.co.architecture.custom.image.loader.domain.mediator

import androidx.compose.ui.graphics.ImageBitmap

/**
 * in-memory 이미지 캐시 인터페이스.
 *
 * 역할
 * - 디코딩된 이미지를 **메모리**에 캐싱/조회해 재사용 비용(네트워크/디코딩)을 줄입니다.
 * - **LRU(Least Recently Used)** 정책을 사용합니다.
 * - 다중 코루틴/스레드에서 동시에 접근 가능하며, **Race Condition**처리가 돼있습니다.
 * - 메모리 압박 시 항목을 퇴출(Eviction)할 수 있습니다. 호출측은 이를 정상 동작으로 간주해야 합니다.
 *
 */
interface ImageMemoryCache {

  /**
   * 주어진 [key]에 대응하는 이미지를 반환합니다.
   *
   * @return 캐시에 존재하면 [ImageBitmap], 없거나 퇴출되었으면 `null`.
   */
  fun get(key: String): ImageBitmap?

  /**
   * [key]로 [image]를 저장합니다.
   *
   * - 동일 키가 이미 존재하면 새 값으로 교체될 수 있습니다.
   * - 캐시 정책/LRU에 따라 다른 항목이 퇴출될 수 있습니다.
   */
  fun put(key: String, image: ImageBitmap)
}
