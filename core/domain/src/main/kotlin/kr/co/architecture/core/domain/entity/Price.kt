package kr.co.architecture.core.domain.entity

import kr.co.architecture.core.domain.enums.SortPriceRangeEnum

sealed interface Price {
  val origin: Int
  @JvmInline
  value class Origin(override val origin: Int) : Price
  data class Discount(override val origin: Int, val discounted: Int) : Price
}

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