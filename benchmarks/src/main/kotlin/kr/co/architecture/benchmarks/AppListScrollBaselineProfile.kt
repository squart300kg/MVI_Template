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
//
//      // PRODUCT_LIST 찾기
//      val productList = device.waitAndFindObject(By.res(PRODUCT_LIST))
//
//      // 수직 스크롤 (Grid, Vertical 대응)
//      repeat(4) { device.fling(element = productList, direction = Direction.DOWN) }
//      repeat(2) { device.fling(element = productList, direction = Direction.UP) }
//
//      // 수평 스크롤용 첫 horizontal 섹션 찾기
//      device.findObject(By.res(Pattern.compile(".*_${HORIZONTAL_ITEMS}")))?.let { horizontalSection ->
//        repeat(2) { device.fling(element = horizontalSection, direction = Direction.RIGHT) }
//        repeat(1) { device.fling(element = horizontalSection, direction = Direction.LEFT) }
//      }
    }
}