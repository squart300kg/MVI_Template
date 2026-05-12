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
sealed interface UiText {
  @Immutable
  data class PlainText(val value: String) : UiText

  @Immutable
  data class Resource(
    @param:StringRes val resId: Int,
    val args: ImmutableList<Any> = persistentListOf()
  ) : UiText

  @Immutable
  data class Composite(
    val parts: ImmutableList<UiText> = persistentListOf()
  ) : UiText

  companion object {
    fun StringResource(
      @StringRes resId: Int,
      args: List<Any> = emptyList()
    ): UiText = Resource(resId = resId, args = args.toImmutableList())

    fun Combined(parts: List<UiText>): UiText =
      Composite(parts = parts.toImmutableList())
  }
}

/**
 * `plus`함수는 UiText.StringResource와 UiText.PlainText를 '+' 연산자로 합치기 기능을 제공
 */
operator fun UiText.plus(other: UiText): UiText =
  UiText.Combined(listOf(this, other))

operator fun UiText.plus(other: String): UiText =
  this + UiText.PlainText(other)

@Composable
fun UiText.asString(): String {
  return when (this) {
    is UiText.PlainText -> value
    is UiText.Resource -> stringResource(resId, *args.toTypedArray())
    is UiText.Composite -> buildString { parts.forEach { append(it.asString()) } }
  }
}

fun UiText.asString(context: Context): String {
  return when (this) {
    is UiText.PlainText -> value
    is UiText.Resource -> context.getString(resId, *args.toTypedArray())
    is UiText.Composite -> buildString { parts.forEach { append(it.asString(context)) } }
  }
}

@Composable
fun UiText.isNotEmpty(): Boolean {
  return asString().isNotEmpty()
}
