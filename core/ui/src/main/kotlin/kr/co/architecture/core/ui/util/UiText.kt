package kr.co.architecture.core.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

data class UiText(
  val value: String? = null,
  @StringRes val resId: Int? = null,
  val args: List<Any> = emptyList(),
  val parts: List<UiText> = emptyList()
) {
  companion object {
    fun DynamicString(value: String) = UiText(value = value)
    fun StringResource(@StringRes resId: Int, args : List<Any> = emptyList()) =
      UiText(resId = resId, args = args)
    fun Combined(parts: List<UiText>) = UiText(parts = parts)
  }
}

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