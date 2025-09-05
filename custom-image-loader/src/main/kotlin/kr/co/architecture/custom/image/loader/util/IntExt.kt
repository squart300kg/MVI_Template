package kr.co.architecture.custom.image.loader.util

fun Int.toReadableSize(): String = when {
  this < 1024 -> "$this B"
  this < 1024 * 1024 -> String.format("%.1f KB", this / 1024.0)
  else -> String.format("%.1f MB", this / (1024.0 * 1024))
}