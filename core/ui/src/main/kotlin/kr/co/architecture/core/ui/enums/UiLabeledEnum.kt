package kr.co.architecture.core.ui.enums

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

/**
 * Dropdown Chip사용 시, 구현하여 사용
 */
@Stable
interface UiLabeledEnum {
  val resId: Int
  val args: String?
}

@Composable
fun UiLabeledEnum.asString(): String =
  args?.let { stringResource(resId, it) } ?: stringResource(resId)