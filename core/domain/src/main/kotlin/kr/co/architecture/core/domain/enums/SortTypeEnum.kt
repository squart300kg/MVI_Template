package kr.co.architecture.core.domain.enums

import kr.co.architecture.core.domain.entity.Price

enum class SortTypeEnum {
  ACCURACY,
  LATEST
}

enum class SortDirectionEnum {
  ASCENDING,
  DESCENDING
}

/**
 * 구간 규칙:
 * - 하한: 초과(>) / 상한: 이하(<=)  → 구간이 절대 겹치지 않음
 *   [.., 5_000], (5_000, 15_000], (15_000, 25_000], (25_000, ..)
 */
enum class PriceRangeSortTypeEnum(
  val minExclusive: Int?,   // null이면 하한 없음
  val maxInclusive: Int?    // null이면 상한 없음
) {
  LTE_5K(null, 5_000),
  GT_5K_LTE_15K(5_000, 15_000),
  GT_15K_LTE_25K(15_000, 25_000),
  GT_25K(25_000, null);

  /** 정수 금액(원) 기준 포함 여부 */
  fun contains(amount: Int): Boolean =
    (minExclusive?.let { amount > it } ?: true) &&
      (maxInclusive?.let { amount <= it } ?: true)

  /** Domain Price 기준 포함 여부 (필터링 편의) */
  fun contains(price: Price): Boolean = contains(price.amountForFilter())

  /**
   * DB 쿼리 등에 쓰기 좋은 “포함 경계값” 반환 (하한 inclusive로 변환)
   * 예) (5_000, 15_000]  →  lowerInclusive = 5_001, upperInclusive = 15_000
   */
  fun inclusiveBounds(): Pair<Int?, Int?> =
    (minExclusive?.plus(1)) to maxInclusive

  companion object {
    /** 금액으로 구간 역탐색 */
    fun of(amount: Int): PriceRangeSortTypeEnum = entries.first { it.contains(amount) }
  }
}

/** Price를 정수 금액으로 뽑아 필터링에 쓰기 위한 도메인 확장 */
private fun Price.amountForFilter(): Int = when (this) {
  is Price.Discount -> discounted
  is Price.Origin -> origin
}