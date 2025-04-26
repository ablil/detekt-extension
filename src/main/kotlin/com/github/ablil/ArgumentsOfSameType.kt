package com.github.ablil

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.psiUtil.getCallNameExpression
import org.jetbrains.kotlin.resolve.calls.util.getType

class ArgumentsOfSameType(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "Reports call of methods with multiple arguments of same type without using named arguments for readability",
        Debt.FIVE_MINS
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        val fnName = expression.getCallNameExpression()?.getReferencedName() ?: return
        val arguments = expression.valueArguments

        val areArgumentsOfSameType = arguments
            .mapNotNull { it.getArgumentExpression()?.getType(bindingContext) }
            .toSet().size == 1
        val hasUnnamedArgument = arguments.any { !it.isNamed() }

        if (arguments.size > 1 && areArgumentsOfSameType && hasUnnamedArgument) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "function %s has all arguments of same type, use named arguments to avoid confusion".format(fnName)
                )
            )
        }
    }

}
