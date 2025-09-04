package kr.co.architecture.custom.image.loader.domain.mediator

import androidx.compose.ui.graphics.ImageBitmap

/**
 * in-memory 이미지 캐시 인터페이스.
 *
 * 역할
 * - 디코딩된 이미지를 **메모리**에 보관/조회해 재사용 비용(네트워크/디코딩)을 줄입니다.
 * - 일반적으로 **LRU(Least Recently Used)** 정책을 권장합니다.
 *
 * 계약(Contract)
 * - **스레드 안전**해야 합니다. (다중 코루틴/스레드에서 동시에 접근 가능)
 * - **논블로킹**이어야 합니다. get/put 호출이 디스크/네트워크/디코딩을 수행하면 안 됩니다.
 * - 저장 키는 호출측이 결정합니다. (예: `"$url@${width}x${height}"` 처럼 사이즈별 분리 권장)
 * - 메모리 압박 시 항목을 퇴출(Eviction)할 수 있습니다. 호출측은 이를 정상 동작으로 간주해야 합니다.
 *
 * 권장 구현 팁
 * - Android에선 `LruCache<String, Bitmap>`/`LruCache<String, ImageBitmap>` 기반 구현이 단순합니다.
 * - 용량 산정은 디바이스/이미지 특성에 따라 달라질 수 있으므로 앱에서 **적정 상한(예: 8~32MB)** 을 주입하세요.
 * - 하드웨어 비트맵(HARDWARE) 등 특수 케이스의 메모리 계산은 구현체에서 주의 깊게 처리하세요.
 *
 * 예시
 * ```
 * class LruImageMemoryCache(maxBytes: Int) : ImageMemoryCache {
 *   private val cache = object : LruCache<String, ImageBitmap>(maxBytes) {
 *     override fun sizeOf(key: String, value: ImageBitmap): Int {
 *       // 구현체에 맞는 추정치/변환 사용
 *       return /* bytes */
 *     }
 *   }
 *   @Synchronized override fun get(key: String) = cache.get(key)
 *   @Synchronized override fun put(key: String, image: ImageBitmap) { cache.put(key, image) }
 * }
 * ```
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
