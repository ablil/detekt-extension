package com.github.ablil

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@KotlinCoreEnvironmentTest
internal class NoDataClassEntityTest(private val env: KotlinCoreEnvironment) {

    @ParameterizedTest
    @ValueSource(
        strings = [
            """
                @Entity
                data class Foo {
                }
            """,
            """
                @jakarata.persistence.Entity
                data class Foo {
                }
            """,

            """
                @Entity("foo")
                data class Foo {
                }
            """,

            """
                @Entity(name = "foo")
                data class Foo {
                }
            """
        ]
    )
    fun `should detekt any code smell`(code: String) {
        val findings = NoDataClassEntity(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }
}
