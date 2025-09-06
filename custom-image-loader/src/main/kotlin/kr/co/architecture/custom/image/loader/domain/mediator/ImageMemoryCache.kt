package kr.co.architecture.custom.image.loader.domain.mediator

import androidx.compose.ui.graphics.ImageBitmap

/**
 * in-memory 이미지 캐시 인터페이스.
 *
 * 역할
 * - 디코딩된 이미지를 **메모리**에 캐싱/조회해 재사용 비용(네트워크/디코딩)을 줄입니다.
 * - **LRU(Least Recently Used)** 정책을 사용합니다.
 * - 다중 코루틴/스레드에서 동시에 접근 가능하며, **Race Condition**처리가 돼있습니다.
 *
 */
interface ImageMemoryCache {

  /**
   * LruCache로부터 [key]에 대응하는 [ImageBitmap]을 반환합니다.
   *
   * @return 캐시에 존재하면 [ImageBitmap], 없거나 퇴출되었으면 `null`.
   */
  fun get(key: String): ImageBitmap?

  /**
   * Native Heap 최대 한도 2GB 안쪽에서 LruCache 정책을 따릅니다.
   *
   * @param key http code 302 응답으로 받은 imageUrl
   * @param image ByteArray에서 디코딩 된 [ImageBitmap]으로 디코딩 된 것
   */
  fun cache(key: String, image: ImageBitmap)
}