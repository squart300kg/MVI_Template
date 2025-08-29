package kr.co.architecture.core.ui.enums

import androidx.annotation.StringRes
import kr.co.architecture.core.domain.enums.SortDirectionEnum
import kr.co.architecture.core.ui.R

enum class SortDirectionUiEnum(
  @StringRes override val resId: Int,
  override val args: String? = null
): BaseSortUiEnum {
  ASCENDING(R.string.ascendingByTitle),
  DESCENDING(R.string.descendingByTitle);

  companion object {
    fun mapperToUi(sortDirectionEnum: SortDirectionEnum) =
      when (sortDirectionEnum) {
        SortDirectionEnum.ASCENDING -> ASCENDING
        SortDirectionEnum.DESCENDING -> DESCENDING
      }

    fun mapperToDomain(sortTypeUiEnum: SortDirectionUiEnum) =
      when (sortTypeUiEnum) {
        ASCENDING -> SortDirectionEnum.ASCENDING
        DESCENDING -> SortDirectionEnum.DESCENDING
      }
  }
}