package kr.co.architecture.benchmarks

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AppListScrollBenchmark {
  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  @Test
  fun startAndScrollProductListWithCompilationNone() = startAndScrollProductList(CompilationMode.None())

  @Test
  fun startAndScrollProductListWithBaselineProfile() = startAndScrollProductList(CompilationMode.Partial())

  @Test
  fun startAndScrollProductListWithFull() = startAndScrollProductList(CompilationMode.Full())

  private fun startAndScrollProductList(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
    packageName = "kr.co.architecture.ssy",
    metrics = listOf(FrameTimingMetric()),
    compilationMode = compilationMode,
    iterations = 1,
    startupMode = StartupMode.WARM,
    setupBlock = {
      pressHome()
      startActivityAndWait()
    }
  ) {
//    // PRODUCT_LIST 찾기
//    val productList = device.waitAndFindObject(By.res(PRODUCT_LIST))
//
//    // 수직 스크롤 (Grid, Vertical 대응)
//    repeat(4) { device.fling(element = productList, direction = Direction.DOWN) }
//    repeat(2) { device.fling(element = productList, direction = Direction.UP) }
//
//    // 수평 스크롤용 첫 horizontal 섹션 찾기
//    device.findObject(By.res(Pattern.compile(".*_${HORIZONTAL_ITEMS}")))?.let { horizontalSection ->
//      repeat(2) { device.fling(element = horizontalSection, direction = Direction.RIGHT) }
//      repeat(1) { device.fling(element = horizontalSection, direction = Direction.LEFT) }
//    }
  }
}