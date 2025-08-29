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
}