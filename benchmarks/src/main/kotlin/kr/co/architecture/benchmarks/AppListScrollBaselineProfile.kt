package kr.co.architecture.benchmarks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import kr.co.architecture.test.testing.ui.SearchTags.HEADER_TEXT_FIELD
import kr.co.architecture.test.testing.ui.SearchTags.bookCard
import org.junit.Rule
import org.junit.Test

@RequiresApi(Build.VERSION_CODES.P)
class AppListScrollBaselineProfile {

  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  @Test
  fun generate() =
    baselineProfileRule.collect("kr.co.architecture.ssy") {
      startActivityAndWait()

      val searchField = device.waitAndFindObject(By.res(HEADER_TEXT_FIELD))
      searchField.click()
      searchField.setText("심리학")

      device.pressEnter()
      device.wait(Until.hasObject(By.descContains(bookCard(""))), 5_000)

      device.pressBack()
      device.waitForIdle()

      val list = device.waitAndFindObject(By.scrollable(true), 5_000)
      repeat(5) { device.fling(list, Direction.DOWN) }
    }
}