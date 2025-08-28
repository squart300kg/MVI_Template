package kr.co.architecture.core.ui.enums

import androidx.annotation.StringRes
import kr.co.architecture.core.domain.enums.SortByDirectionEnum
import kr.co.architecture.core.ui.R

enum class SortDirectionUiEnum(@StringRes val resId: Int) {
  ASCENDING(R.string.ascendingByTitle),
  DESCENDING(R.string.descendingByTitle);

  companion object {
    fun mapperToUi(sortByDirectionEnum: SortByDirectionEnum) =
      when (sortByDirectionEnum) {
        SortByDirectionEnum.ASCENDING -> ASCENDING
        SortByDirectionEnum.DESCENDING -> DESCENDING
      }

    fun mapperToDomain(sortTypeUiEnum: SortDirectionUiEnum) =
      when (sortTypeUiEnum) {
        ASCENDING -> SortByDirectionEnum.ASCENDING
        DESCENDING -> SortByDirectionEnum.DESCENDING
      }
  }
}