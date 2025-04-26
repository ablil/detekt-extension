package com.github.ablil

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class NoDataClassEntity(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Maintainability,
        description = "Avoid using Kotlin's data classes with JPA entities",
        debt = Debt.TWENTY_MINS
    )


    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.isData() && klass.annotationEntries.any { it.shortName?.asString()?.endsWith("Entity") == true }) {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(klass),
                    message = "Avoid using Kotlin data classes with JPA entities, use normal class instead"
                )
            )
        }
    }
}
