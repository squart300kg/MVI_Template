package kr.co.architecture.custom.image.loader.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.Locale

fun hashSha256(s: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(s.toByteArray(Charsets.UTF_8))
    val sb = StringBuilder(bytes.size * 2)
    for (b in bytes) {
      val v = b.toInt() and 0xFF
      if (v < 16) sb.append('0')
      sb.append(Integer.toHexString(v))
    }
    return sb.toString()
  }

suspend fun ByteArray.decodeToImageBitmap(): ImageBitmap? =
  withContext(Dispatchers.Default) {
    BitmapFactory.decodeByteArray(this@decodeToImageBitmap, 0, size)?.asImageBitmap()
  }

fun mergedHeader(
  requestHeader: Map<String, String>,
  responseHeader: Map<String, String>
): Map<String, String> =
  buildMap {
    putAll(requestHeader.mapKeys { it.key.lowercase(Locale.ROOT) })
    responseHeader.forEach { (k, v) -> put(k.lowercase(Locale.ROOT), v) }
  }