package kr.co.architecture.benchmarks

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import kr.co.architecture.test.testing.ui.SearchTags.HEADER_TEXT_FIELD
import kr.co.architecture.test.testing.ui.SearchTags.bookCard
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

      val searchField = device.waitAndFindObject(By.res(HEADER_TEXT_FIELD))
      searchField.click()
      searchField.setText("심리학")

      device.pressEnter()
      device.wait(Until.hasObject(By.descContains(bookCard(""))), 5_000)

      device.pressBack()
      device.waitForIdle()
    }
  ) {
    val list = device.waitAndFindObject(By.scrollable(true), 5_000)
    repeat(5) { device.fling(list, Direction.DOWN) }
  }
}