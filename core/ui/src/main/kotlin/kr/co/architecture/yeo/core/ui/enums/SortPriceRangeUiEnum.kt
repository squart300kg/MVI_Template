package kr.co.architecture.yeo.core.ui.enums

import androidx.annotation.StringRes
import kr.co.architecture.yeo.core.domain.enums.SortPriceRangeEnum
import kr.co.architecture.yeo.core.ui.R

enum class SortPriceRangeUiEnum(
  @StringRes override val resId: Int,
  override val args: String?
): BaseSortUiEnum {
  ALL(R.string.sortTypePriceAll, null),
  LESS_THAN_10000(R.string.priceRangeLess, "10,000"),
  MORE_THAN_10000(R.string.priceRangeMore, "10,000");

  companion object {
    fun mapperToDomain(sortUiEnum: SortPriceRangeUiEnum) =
      when (sortUiEnum) {
        ALL -> SortPriceRangeEnum.ALL
        LESS_THAN_10000 -> SortPriceRangeEnum.LESS
        MORE_THAN_10000 -> SortPriceRangeEnum.MORE
      }
  }
}