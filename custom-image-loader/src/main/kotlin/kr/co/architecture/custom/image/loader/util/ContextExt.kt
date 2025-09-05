package kr.co.architecture.custom.image.loader.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP

fun Context.computeAppAvailableHeap(hardCapMb: Int = 110): Int {
  val am = getSystemService(ActivityManager::class.java)
  val isLowRam = am?.isLowRamDevice == true
  val heapMb = if ((applicationInfo.flags and FLAG_LARGE_HEAP) != 0)
    (am?.largeMemoryClass ?: am?.memoryClass ?: 256)
  else
    (am?.memoryClass ?: 256)

  val fraction = if (isLowRam) 0.12 else 0.20      // 힙의 12% (저사양) / 20% (일반)
  val targetMb = (heapMb * fraction).toInt()
  val cappedMb = minOf(targetMb, hardCapMb)
  return cappedMb * 1024 * 1024                     // bytes
}