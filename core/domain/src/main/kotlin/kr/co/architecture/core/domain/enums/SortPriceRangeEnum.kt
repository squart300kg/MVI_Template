package kr.co.architecture.core.domain.enums

enum class SortPriceRangeEnum {
  ALL,
  LESS,
  MORE;

  fun contains(amount: Int, threshold: Int = 10_000): Boolean = when (this) {
    ALL -> true
    LESS -> amount < threshold
    MORE -> amount >= threshold
  }

  // TODO: 무쓸모 지우기
  fun inclusiveBounds(threshold: Int = 10_000): Pair<Int?, Int?> = when (this) {
    ALL -> 0 to null
    LESS -> null to (threshold - 1)
    MORE -> threshold to null
  }
}