package net.meilcli.hubber.toolchain.config.android.module

import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

abstract class BaseDataModulePlugin : BasePlugin() {

    override fun apply(project: Project) {
        project.applyPlugin()
        super.apply(project)

        AndroidNamespace.apply(project)
    }

    private fun Project.applyPlugin() {
        plugins.apply("com.android.library")
        plugins.apply("org.jetbrains.kotlin.android")
        plugins.apply("org.jetbrains.kotlin.kapt")
        plugins.apply("org.jetbrains.kotlin.plugin.parcelize")
        plugins.apply("org.jetbrains.dokka")
        plugins.apply("com.google.dagger.hilt.android")
        plugins.apply("com.google.devtools.ksp")
        plugins.apply("io.gitlab.arturbosch.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.android")
        plugins.apply("net.meilcli.hubber.toolchain.config.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.dokka")
        plugins.apply("net.meilcli.hubber.toolchain.config.dagger")
        plugins.apply("net.meilcli.hubber.toolchain.config.ksp")
    }

    override fun DependencyHandler.applyDependencies() {
        setupKotlin()
        setupAndroidData()
        setupTest()
    }
}
