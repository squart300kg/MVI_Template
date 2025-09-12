package kr.co.architecture.yeo.core.domain.entity

import kr.co.architecture.yeo.core.domain.enums.SortPriceRangeEnum
import kr.co.architecture.yeo.core.domain.enums.SortPriceRangeEnum2

sealed interface Price {
  val origin: Int
  @JvmInline
  value class Origin(override val origin: Int) : Price
  data class Discount(override val origin: Int, val discounted: Int) : Price
}

fun Price.matches2(range: SortPriceRangeEnum2): Boolean {
  val minPrice = range.minPrice
  val maxPrice = range.maxPrice
  return when {
    minPrice == null && maxPrice == null -> true
    minPrice == null && maxPrice != null -> toAmount() <= maxPrice
    minPrice != null && maxPrice != null -> toAmount() >= minPrice && toAmount() <= maxPrice
    minPrice != null && maxPrice == null -> toAmount() >= minPrice
    else -> error("unreachable code")
  }
}


// TODO: 만약, 가격 정렬이 '이상'/'이하'가 아닌, '중간 구간'이 존재한다면 어떻게 바꿀것인가?
fun Price.matches(range: SortPriceRangeEnum, threshold: Int): Boolean = when (range) {
  SortPriceRangeEnum.ALL  -> true
  SortPriceRangeEnum.LESS -> toAmount() <  threshold
  SortPriceRangeEnum.MORE -> toAmount() >= threshold
}

fun Price.toAmount(): Int =
  when (this) {
    is Price.Discount -> discounted
    is Price.Origin -> origin
  }