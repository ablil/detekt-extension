package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@KotlinCoreEnvironmentTest
internal class DescriptiveTestFunctionsTest(private val env: KotlinCoreEnvironment) {

    @Test
    fun `report usage of invalid test function name`() {
        val code = """
            
            class MyTestClass {
            
                @Test
                fun `random test function`() {
                }
            }
        """.trimIndent()
        val findings = DescriptiveTestFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "should check something",
        "should check something when something happens",
        "should check something given something",
        "given something then check something",
        "given something when something happens then check something",
    ])
    fun `should not report any finding given valid test function names`(fnName: String) {
        val code = """
            
            class MyTestClass {
            
                @Test
                fun `$fnName`() {
                }
            }
        """.trimIndent()
        val findings = DescriptiveTestFunctions(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
