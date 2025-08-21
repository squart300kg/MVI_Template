package kr.co.architecture.benchmarks

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.uiautomator.By
import kr.co.architecture.test.testing.util.ALIM_CENTER_LIST
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
  fun startAndScrollProductListWithCompilationDefault() = startAndScrollProductList(CompilationMode.DEFAULT)

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
    val alimCenterList = device.waitAndFindObject(By.res(ALIM_CENTER_LIST))
    repeat(3) {
      device.flingElementDown(alimCenterList)
      device.waitForIdle()
    }
  }
}