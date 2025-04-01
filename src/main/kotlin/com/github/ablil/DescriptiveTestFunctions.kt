package com.github.ablil

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtNamedFunction

class DescriptiveTestFunctions(config: Config) : Rule(config) {

    private val shouldWhen = Regex("^should.*(when|given)?.*$")
    private val givenThen = Regex("^given.*then.*$")
    private val givenWhenThen = Regex("^given.*when.*then.*$")

    private val testAnnotations = setOf("Test", "ParameterizedTest")

    override val issue: Issue = Issue(
        javaClass.simpleName,
        Severity.Maintainability,
        "Test function name is not descriptive enough",
        Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val fnName = function.name ?: return

        if (isTestFunction(function) && !isValidFunctionName(fnName)) {
            report(
                CodeSmell(
                    issue,
                    Entity.Companion.from(function),
                    "detected sketchy fn name [$fnName]"
                )
            )
        }

    }

    private fun isValidFunctionName(name: String): Boolean =
        shouldWhen.matches(name) || givenThen.matches(name) || givenWhenThen.matches(name)

    private fun isTestFunction(function: KtNamedFunction): Boolean =
        function.annotationEntries.mapNotNull { it.shortName?.asString()?.split(".")?.last() }
            .any { testAnnotations.contains(it) }
}
