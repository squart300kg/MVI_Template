package kr.co.architecture.yeo.core.ui.enums

import androidx.annotation.StringRes
import kr.co.architecture.yeo.core.domain.enums.SortEnum
import kr.co.architecture.yeo.core.ui.R

enum class SortUiEnum(
  @StringRes override val resId: Int,
  override val args: String? = null
): BaseSortUiEnum {
  ACCURACY(R.string.sortTypeAccuracy),
  LATEST(R.string.sortTypeLatest);

  companion object {
    fun mapperToUi(sortEnum: SortEnum) =
      when (sortEnum) {
        SortEnum.ACCURACY -> ACCURACY
        SortEnum.LATEST -> LATEST
      }

    fun mapperToDomain(sortUiEnum: SortUiEnum) =
      when (sortUiEnum) {
        ACCURACY -> SortEnum.ACCURACY
        LATEST -> SortEnum.LATEST
      }
  }
}