package kr.co.architecture.core.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class UiText(
  val value: String? = null,
  @StringRes val resId: Int? = null,
  val args: ImmutableList<Any> = persistentListOf(),
  val parts: ImmutableList<UiText> = persistentListOf()
) {
  companion object {
    fun DynamicString(value: String) = UiText(value = value)
    fun StringResource(@StringRes resId: Int, args : List<Any> = emptyList()) =
      UiText(resId = resId, args = args.toImmutableList())
    fun Combined(parts: List<UiText>) = UiText(parts = parts.toImmutableList())
  }
}

/**
 * `plus`함수는 UiText.StringResource와 UiText.DynamicString를 '+' 연산자를 통해 합치기 기능을 제공
 */
operator fun UiText.plus(other: UiText): UiText =
  UiText.Combined(listOf(this, other))

operator fun UiText.plus(other: String): UiText =
  this + UiText.DynamicString(other)

operator fun String.plus(other: UiText): UiText =
  UiText.DynamicString(this) + other

@Composable
fun UiText.asString(): String {
  return when {
    value != null -> value
    resId != null -> stringResource(resId, *args.toTypedArray())
    parts.isNotEmpty() -> buildString { parts.forEach { append(it.asString()) } }
    else -> ""
  }
}

fun UiText.asString(context: Context): String {
  return when {
    value != null -> value
    resId != null -> context.getString(resId, *args.toTypedArray())
    parts.isNotEmpty() -> buildString { parts.forEach { append(it.asString(context)) } }
    else -> ""
  }
}

@Composable
fun UiText.isNotEmpty(): Boolean {
  return when {
    value != null -> value.isNotEmpty()
    resId != null -> stringResource(resId, *args.toTypedArray()).isNotEmpty()
    parts.isNotEmpty() -> parts.any { it.isNotEmpty() }
    else -> false
  }
}