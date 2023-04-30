package net.meilcli.hubber.toolchain.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.junit.Assert.assertEquals
import org.junit.Test

class InternalVisibilityTest {

    @Test
    fun testWarning_whenPublicObject() {
        val findings = InternalVisibility(Config.empty).compileAndLint(
            """
                package net.meilcli.hubber.detekt.internal

                object Message
            """.trimIndent()
        )

        assertEquals(1, findings.size)
    }

    @Test
    fun testWarning_whenPublicClass() {
        val findings = InternalVisibility(Config.empty).compileAndLint(
            """
                package net.meilcli.hubber.detekt.internal

                class Message
            """.trimIndent()
        )

        assertEquals(1, findings.size)
    }

    @Test
    fun testWarning_whenPublicInterface() {
        val findings = InternalVisibility(Config.empty).compileAndLint(
            """
                package net.meilcli.hubber.detekt.internal

                interface IMessage
            """.trimIndent()
        )

        assertEquals(1, findings.size)
    }

    @Test
    fun testWarning_whenPublicProperty() {
        val findings = InternalVisibility(Config.empty).compileAndLint(
            """
                package net.meilcli.hubber.detekt.internal

                val message = "hello world"
            """.trimIndent()
        )

        assertEquals(1, findings.size)
    }

    @Test
    fun testWarning_whenPublicFunction() {
        val findings = InternalVisibility(Config.empty).compileAndLint(
            """
                package net.meilcli.hubber.detekt.internal

                fun message(): String = "hello world"
            """.trimIndent()
        )

        assertEquals(1, findings.size)
    }

    @Test
    fun testNoWarning() {
        val findings = InternalVisibility(Config.empty).compileAndLint(
            """
                package net.meilcli.hubber.detekt.internal
                
                internal object MessageObject
                internal class MessageClass
                internal interface IMessage
        
                internal val message = "hello world"

                internal fun message(): String = "hello world"
            """.trimIndent()
        )

        assertEquals(0, findings.size)
    }
}
