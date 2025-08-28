package kr.co.architecture.core.ui.enums

import androidx.annotation.StringRes
import kr.co.architecture.core.domain.enums.SortTypeEnum
import kr.co.architecture.core.ui.R

enum class SortTypeUiEnum(@StringRes val resId: Int) {
  ACCURACY(R.string.sortTypeAccuracy),
  LATEST(R.string.sortTypeLatest);

  companion object {
    fun mapperToUi(sortTypeEnum: SortTypeEnum) =
      when (sortTypeEnum) {
        SortTypeEnum.ACCURACY -> ACCURACY
        SortTypeEnum.LATEST -> LATEST
      }

    fun mapperToDomain(sortTypeUiEnum: SortTypeUiEnum) =
      when (sortTypeUiEnum) {
        ACCURACY -> SortTypeEnum.ACCURACY
        LATEST -> SortTypeEnum.LATEST
      }
  }
}