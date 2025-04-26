package com.github.ablil

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class ArgumentsOfSameTypeTest(private val env: KotlinCoreEnvironment) {

    @Test
    fun `should report code smell given primitive args of same type`() {
        val code = """
            fun test1() {
                fun move(x: Int, y: Int, z: Int) {}
                move(1, 2, 3) // Violation
            }
        """.trimIndent()

        val findings = ArgumentsOfSameType(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `should report code smell given custom types`() {
        val code = """
            data class Coordinate(val value: Double)

            // Should trigger violation - all Coordinate parameters without named arguments
            fun test4() {
                fun plot(start: Coordinate, end: Coordinate) {}
                plot(Coordinate(0.0), Coordinate(1.0)) // Violation
            }      
            """.trimIndent()

        val findings = ArgumentsOfSameType(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `should report code smell given partial named args and same type`() {
        val code = """
            // Should trigger violation - some named, some not
            fun test13() {
                fun setDimensions(width: Int, height: Int, depth: Int) {}
                setDimensions(100, height = 200, 300) // Violation
            }
        """.trimIndent()
        val findings = ArgumentsOfSameType(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

}
