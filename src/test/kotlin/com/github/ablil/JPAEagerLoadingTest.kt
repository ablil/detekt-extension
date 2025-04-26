package com.github.ablil

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class JPAEagerLoadingTest(private val env: KotlinCoreEnvironment) {


    @Test
    fun `should report implicit eager loading`() {
        val code = """
            @Entity
            class Order {
                @ManyToOne // Defaults to EAGER - violation
                val customer: Customer? = null

                @OneToOne // Defaults to EAGER - violation
                val price: Price? = null
            }
        """.trimIndent()

        val findings = JPAEagerLoading(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 2
    }

    @Test
    fun `should report explicit eager loading`() {
        val code = """
            @Entit@Entity
            class Product {
                @OneToMany(fetch = FetchType.EAGER) // Explicit EAGER - violation
                val components: List<Component> = emptyList()
            }
        """.trimIndent()

        val findings = JPAEagerLoading(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }
}
