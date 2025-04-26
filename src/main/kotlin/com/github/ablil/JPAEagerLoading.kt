package com.github.ablil

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.isPublic


class JPAEagerLoading(config: Config) : Rule(config) {
    override val issue: Issue = Issue(
        javaClass.simpleName,
        Severity.Performance,
        "Eager loading detected in JPA entity. Prefer lazy loading for better performance.",
        Debt.TWENTY_MINS
    )

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)

        if (isJpaEntity(klass)) {
            klass.getProperties().forEach { property ->
                if (eagerlyLoaded(property)) {
                    report(
                        CodeSmell(
                            issue,
                            Entity.from(property),
                            "association %s is eagerly loaded, consider FetchType.LAZY for better performance"
                        )
                    )
                }
            }
        }
    }

    private fun eagerlyLoaded(property: KtProperty): Boolean {
        if (!property.isPublic) {
            // skip private properties, JPA maps only public properties
            return false
        }

        val associationAnnotations = property.annotationEntries
            .filter { annotation -> ASSOCIATION_ANNOTATIONS.any { annotation.shortName?.asString() == it } }
        val hasExplicitEagerLoading = associationAnnotations.any { annotation ->
            annotation.valueArguments.mapNotNull { it.getArgumentExpression()?.text }.contains("FetchType.EAGER")
        }

        val hasImplicitEagerLoading =
            associationAnnotations.filter { it.shortName?.asString()?.endsWith("ToOne") == true }
                .any { annotation ->
                    !annotation.valueArguments.mapNotNull { it.getArgumentName()?.asName?.toString() }.contains("fetch")
                }

        return hasExplicitEagerLoading || hasImplicitEagerLoading
    }

    private fun isJpaEntity(klass: KtClass): Boolean =
        klass.annotationEntries.any { it.shortName?.asString()?.endsWith("Entity") == true }

    companion object {
        val ASSOCIATION_ANNOTATIONS = setOf("OneToMany", "ManyToOne", "ManyToMany", "OneToOne")
    }
}
