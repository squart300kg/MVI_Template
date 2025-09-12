package kr.co.architecture.yeo.core.domain.enums

data class SortPriceRangeEnum2(
  val minPrice: Int? = null,
  val maxPrice: Int? = null
)

enum class SortPriceRangeEnum {
  ALL,
  LESS,
  MORE;

//  fun contains(amount: Int, threshold: Int = 10_000): Boolean = when (this) {
//    ALL -> true
//    LESS -> amount < threshold
//    MORE -> amount >= threshold
//  }
}