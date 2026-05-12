package kr.co.architecture.quality.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Assert.assertEquals
import org.junit.Test

class FeatureArchitectureRulesTest {

  @Test
  fun `reports direct feature navigation usage`() {
    val code = """
      package kr.co.architecture.feature.sample

      import androidx.navigation.NavHostController

      fun SampleScreen(navController: NavHostController) {
        navController.navigate("sample")
      }
    """.trimIndent()

    val findings = NoFeatureDirectNavigationRule(Config.empty).lint(code)

    assertEquals(3, findings.size)
  }

  @Test
  fun `allows viewmodel navigation method names`() {
    val code = """
      package kr.co.architecture.feature.sample

      fun SampleScreen(viewModel: SampleViewModel) {
        viewModel.navigateBack()
      }

      class SampleViewModel {
        fun navigateBack() = Unit
      }
    """.trimIndent()

    val findings = NoFeatureDirectNavigationRule(Config.empty).lint(code)

    assertEquals(0, findings.size)
  }

  @Test
  fun `reports direct feature global ui usage`() {
    val code = """
      package kr.co.architecture.feature.sample

      import kr.co.architecture.core.ui.BaseProgressBar

      fun SampleScreen() {
        BaseProgressBar(true)
      }
    """.trimIndent()

    val findings = NoFeatureGlobalUiDirectUsageRule(Config.empty).lint(code)

    assertEquals(2, findings.size)
  }
}
