package kr.co.architecture.quality.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Assert.assertEquals
import org.junit.Test

class FeatureArchitectureRulesTest {

  @Test
  fun `feature 화면에서 navigation 객체를 직접 사용하면 위반으로 보고한다`() {
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
  fun `viewmodel navigation method 호출은 허용한다`() {
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
  fun `feature 화면에서 전역 ui를 직접 렌더링하면 위반으로 보고한다`() {
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
