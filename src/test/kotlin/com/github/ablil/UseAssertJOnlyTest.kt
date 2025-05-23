package com.github.ablil

import com.github.ablil.UseAssertJOnly
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class UseAssertJOnlyTest(private val env: KotlinCoreEnvironment) {

    @Test
    fun `reports usage of kotlin test package`() {
        val code = """
        import io.kotest.matchers.collections.shouldHaveSize
        import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
        import org.junit.jupiter.api.Test
        import kotlin.test.assertEquals

        class Foo {
        
            @Test
            fun testing() {
                assertEquals("foo", "foo")
            }
        }
        """
        val findings = UseAssertJOnly(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `doesn't report inner classes`() {
        val code = """
        class A {
          class B
        }
        """
        val findings = UseAssertJOnly(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }
}
