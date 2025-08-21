package kr.co.architecture.benchmarks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
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