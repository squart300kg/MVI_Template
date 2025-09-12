package kr.co.architecture.yeo.core.ui.enums

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

/**
 * Dropdown Chip사용 시, 구현하여 사용
 */
@Stable
interface BaseSortUiEnum {
  val resId: Int
  val args: String?
}

@Stable
interface BaseSortUiEnum2 {
  val minResId: Int?
  val maxResId: Int?
  val args: String?
}

@Composable
fun BaseSortUiEnum.asString(): String =
  args?.let { stringResource(resId, it) } ?: stringResource(resId)