package kr.co.architecture.quality.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class MviTemplateStyleRuleSetProvider : RuleSetProvider {
  override val ruleSetId: String = RULE_SET_ID

  override fun instance(config: Config): RuleSet {
    return RuleSet(
      id = ruleSetId,
      rules = listOf(
        NoFeatureDirectNavigationRule(config),
        NoFeatureGlobalUiDirectUsageRule(config),
      ),
    )
  }

  private companion object {
    const val RULE_SET_ID = "mvi-template-style"
  }
}
