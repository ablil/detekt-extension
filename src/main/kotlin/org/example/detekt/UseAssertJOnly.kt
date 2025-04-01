package org.example.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue

class UseAssertJOnly(config: Config) : Rule(config) {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Maintainability,
        "Use AssertJ library for assertions",
        Debt.FIVE_MINS,
    )

    override fun visitImportDirective(importDirective: KtImportDirective) {
        super.visitImportDirective(importDirective)

        val fqName = importDirective.importedFqName?.asString() ?: return
        bannedAssertions.any { fqName.startsWith(it) }.ifTrue {
            report(
                CodeSmell(
                    issue,
                    Entity.from(importDirective),
                    "Usage of $fqName detected, use AssertJ instead"
                )
            )
        }
    }


    companion object {
        val bannedAssertions = setOf(
            "kotlin.test.assertTrue",
            "kotlin.test.assertFalse",
            "kotlin.test.assertEquals",
            "kotlin.test.assertNotEquals",
            "kotlin.test.assertNull",
            "kotlin.test.assertNotNull",
            "org.junit.jupiter.api.Assertions.assertTrue",
            "org.junit.jupiter.api.Assertions.assertFalse",
            "org.junit.jupiter.api.Assertions.assertEquals",
            "org.junit.jupiter.api.Assertions.assertNotEquals",
            "org.junit.jupiter.api.Assertions.assertNull",
            "org.junit.jupiter.api.Assertions.assertNotNull",
            "org.hamcrest.MatcherAssert.assertThat"
        )
    }
}
