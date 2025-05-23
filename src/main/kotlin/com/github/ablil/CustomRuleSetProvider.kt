package com.github.ablil

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CustomRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "CustomRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                UseAssertJOnly(config),
                DescriptiveTestFunctions(config),
                NoDataClassEntity(config),
                ArgumentsOfSameType(config),
                JPAEagerLoading(config)
            ),
        )
    }
}
