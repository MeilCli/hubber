package net.meilcli.hubber.toolchain.dokka

import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.Assert.assertEquals
import org.junit.Test

class HideNoDocumentedPluginTest : BaseAbstractTest() {

    private val configuration = dokkaConfiguration {
        sourceSets {
            sourceSet {
                sourceRoots = listOf("src")
            }
        }
    }

    @Test
    fun hideDocumentation_topLevelFunction() {
        testInline(
            """
                |/src/main/Testing.kt
                |package testing
                |
                |fun hide() {}
                |
                |/**
                | * Show
                | */
                |fun show() {}
            """.trimIndent(),
            configuration
        ) {
            preMergeDocumentablesTransformationStage = { modules ->
                val actual = modules.flatMap { it.packages }.flatMap { it.functions }
                assertEquals(1, actual.size)
                assertEquals("show", actual[0].name)
            }
        }
    }

    @Test
    fun hideDocumentation_topLevelProperty() {
        testInline(
            """
                |/src/main/Testing.kt
                |package testing
                |
                |val hide: String = ""
                |
                |/**
                | * Show
                | */
                |val show: String = ""
            """.trimIndent(),
            configuration
        ) {
            preMergeDocumentablesTransformationStage = { modules ->
                val actual = modules.flatMap { it.packages }.flatMap { it.properties }
                assertEquals(1, actual.size)
                assertEquals("show", actual[0].name)
            }
        }
    }

    @Test
    fun hideDocumentation_topLevelClasslike() {
        testInline(
            """
                |/src/main/Testing.kt
                |package testing
                |
                |class Hide
                |
                |/**
                | * Show
                | */
                |class Show
            """.trimIndent(),
            configuration
        ) {
            preMergeDocumentablesTransformationStage = { modules ->
                val actual = modules.flatMap { it.packages }.flatMap { it.classlikes }
                assertEquals(1, actual.size)
                assertEquals("Show", actual[0].name)
            }
        }
    }

    @Test
    fun hideDocumentation_memberLevelFunction() {
        testInline(
            """
                |/src/main/Testing.kt
                |package testing
                |
                |class Hide {
                |
                |    fun hide() {}
                |}
                |
                |/**
                | * Show
                | */
                |class Show {
                |
                |    fun hide() {}
                |
                |    /**
                |     * Show
                |     */
                |    fun show() {}
                |}
                |
                |class ShowBecauseMember {
                |
                |    /**
                |     * Show
                |     */
                |    fun show() {}
                |}
            """.trimIndent(),
            configuration
        ) {
            preMergeDocumentablesTransformationStage = { modules ->
                val actual = modules.flatMap { it.packages }.flatMap { it.classlikes }
                assertEquals(2, actual.size)
                assertEquals("Show", actual[0].name)
                assertEquals(2, actual[0].children.size)
                assertEquals("show", actual[0].children[0].name)
                assertEquals("Show", actual[0].children[1].name)
                assertEquals("ShowBecauseMember", actual[1].name)
                assertEquals(1, actual[1].children.size)
                assertEquals("show", actual[1].children[0].name)
            }
        }
    }

    @Test
    fun hideDocumentation_memberLevelProperty() {
        testInline(
            """
                |/src/main/Testing.kt
                |package testing
                |
                |class Hide {
                |
                |    val hide: String = ""
                |}
                |
                |/**
                | * Show
                | */
                |class Show {
                |
                |    val hide: String = ""
                |
                |    /**
                |     * Show
                |     */
                |    val show: String = ""
                |}
                |
                |class ShowBecauseMember {
                |
                |    /**
                |     * Show
                |     */
                |    val show: String = ""
                |}
            """.trimIndent(),
            configuration
        ) {
            preMergeDocumentablesTransformationStage = { modules ->
                val actual = modules.flatMap { it.packages }.flatMap { it.classlikes }
                assertEquals(2, actual.size)
                assertEquals("Show", actual[0].name)
                assertEquals(2, actual[0].children.size)
                assertEquals("show", actual[0].children[0].name)
                assertEquals("Show", actual[0].children[1].name)
                assertEquals("ShowBecauseMember", actual[1].name)
                assertEquals(1, actual[1].children.size)
                assertEquals("show", actual[1].children[0].name)
            }
        }
    }
}
