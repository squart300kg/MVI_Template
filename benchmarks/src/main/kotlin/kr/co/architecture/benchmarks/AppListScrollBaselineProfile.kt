package kr.co.architecture.benchmarks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import kr.co.kurly.benchmarks.fling
import kr.co.kurly.benchmarks.waitAndFindObject
import org.junit.Rule
import org.junit.Test
import java.util.regex.Pattern

@RequiresApi(Build.VERSION_CODES.P)
class AppListScrollBaselineProfile {

  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  @Test
  fun generate() =
    baselineProfileRule.collect("kr.co.architecture.ssy") {
      startActivityAndWait()

      val list = device.waitAndFindObject(By.scrollable(true), 5_000)
      repeat(5) { device.fling(list, Direction.DOWN) }
    }
}