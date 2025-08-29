package kr.co.architecture.core.domain.enums

enum class SortPriceRangeEnum {
  LESS,
  MORE;

  fun contains(amount: Int, threshold: Int = 10_000): Boolean = when (this) {
    LESS -> amount < threshold
    MORE -> amount >= threshold
  }

  fun inclusiveBounds(threshold: Int = 10_000): Pair<Int?, Int?> = when (this) {
    LESS -> null to (threshold - 1)
    MORE -> threshold to null
  }
}