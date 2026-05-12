package kr.co.architecture.quality.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.psiUtil.getQualifiedExpressionForSelector

internal abstract class PackageScopedRule(
  config: Config,
) : Rule(config) {
  protected fun isRuleActive(): Boolean = valueOrDefault("active", true)
  private fun targetPackage(): String = valueOrDefault("targetPackage", DEFAULT_TARGET_PACKAGE)

  protected fun KtElement.isTargetPackage(): Boolean {
    return containingKtFile.packageFqName.asString().startsWith(targetPackage())
  }

  protected fun KtImportDirective.isTargetPackage(): Boolean {
    return containingKtFile.packageFqName.asString().startsWith(targetPackage())
  }

  protected fun KtParameter.isTargetPackage(): Boolean {
    return containingKtFile.packageFqName.asString().startsWith(targetPackage())
  }

  private companion object {
    const val DEFAULT_TARGET_PACKAGE = "kr.co.architecture.feature"
  }
}

/**
 * Feature 화면에서 navigation 객체를 직접 소유하는 코드를 막습니다.
 * 화면 이동은 ViewModel의 navigation method로 모읍니다.
 */
internal class NoFeatureDirectNavigationRule(
  config: Config,
) : PackageScopedRule(config) {
  override val issue: Issue = Issue(
    id = "NoFeatureDirectNavigation",
    severity = Severity.Defect,
    description = "Feature code must request navigation through BaseViewModel navigation methods.",
    debt = Debt.FIVE_MINS,
  )

  override fun visitImportDirective(importDirective: KtImportDirective) {
    super.visitImportDirective(importDirective)
    if (!isRuleActive() || !importDirective.isTargetPackage()) return

    val importedName = importDirective.importedFqName?.asString().orEmpty()
    if (navigationImports.none(importedName::contains)) return

    reportViolation(importDirective)
  }

  override fun visitParameter(parameter: KtParameter) {
    super.visitParameter(parameter)
    if (!isRuleActive() || !parameter.isTargetPackage()) return

    val typeText = parameter.typeReference?.text.orEmpty()
    if (navigationTypes.none(typeText::contains)) return

    reportViolation(parameter)
  }

  override fun visitCallExpression(expression: KtCallExpression) {
    super.visitCallExpression(expression)
    if (!isRuleActive() || !expression.isTargetPackage()) return

    val qualifiedExpression = expression.getQualifiedExpressionForSelector() ?: return
    val receiverText = qualifiedExpression.receiverExpression.text
    val calleeText = expression.calleeExpression?.text.orEmpty()
    val isDirectCall = receiverText.contains("navController") &&
      calleeText in directNavigationCalls
    val isBackStackCall = calleeText == "popBackStack"

    if (!isDirectCall && !isBackStackCall) return

    reportViolation(expression)
  }

  private fun reportViolation(element: KtElement) {
    report(
      CodeSmell(
        issue = issue,
        entity = Entity.from(element),
        message =
          "Feature 화면 이동은 ViewModel의 navigateTo, navigateBack, navigateWeb 메서드로 요청하세요. " +
            ".ai-skills/ui-layer/ui-navigation-guide.md를 참고하세요.",
      ),
    )
  }

  private companion object {
    val navigationImports = setOf(
      "androidx.navigation.NavController",
      "androidx.navigation.NavHostController",
      "androidx.compose.ui.platform.LocalUriHandler",
    )
    val navigationTypes = setOf("NavController", "NavHostController")
    val directNavigationCalls = setOf("navigate", "popBackStack")
  }
}

/**
 * Feature 화면에서 전역 loading/error UI를 직접 렌더링하는 코드를 막습니다.
 * 공통 loading/error 표시는 ViewModel의 GlobalUiBus 경로로 모읍니다.
 */
internal class NoFeatureGlobalUiDirectUsageRule(
  config: Config,
) : PackageScopedRule(config) {
  override val issue: Issue = Issue(
    id = "NoFeatureGlobalUiDirectUsage",
    severity = Severity.Defect,
    description = "Feature code must use GlobalUiBus through ViewModel for global loading and error UI.",
    debt = Debt.FIVE_MINS,
  )

  override fun visitImportDirective(importDirective: KtImportDirective) {
    super.visitImportDirective(importDirective)
    if (!isRuleActive() || !importDirective.isTargetPackage()) return

    val importedName = importDirective.importedFqName?.asString().orEmpty()
    if (globalUiTypes.none(importedName::contains)) return

    reportViolation(importDirective)
  }

  override fun visitCallExpression(expression: KtCallExpression) {
    super.visitCallExpression(expression)
    if (!isRuleActive() || !expression.isTargetPackage()) return

    val calleeText = expression.calleeExpression?.text.orEmpty()
    if (calleeText !in globalUiTypes) return

    reportViolation(expression)
  }

  private fun reportViolation(element: KtElement) {
    report(
      CodeSmell(
        issue = issue,
        entity = Entity.from(element),
        message =
          "전역 progress/error UI는 feature에서 직접 렌더링하지 말고 ViewModel의 GlobalUiBus 경로를 사용하세요. " +
            ".ai-skills/ui-layer/ui-state-viewmodel-guide.md와 .ai-skills/ui-layer/ui-dialog-bottomsheet-guide.md를 참고하세요.",
      ),
    )
  }

  private companion object {
    val globalUiTypes = setOf("BaseProgressBar", "BaseCenterDialog")
  }
}
