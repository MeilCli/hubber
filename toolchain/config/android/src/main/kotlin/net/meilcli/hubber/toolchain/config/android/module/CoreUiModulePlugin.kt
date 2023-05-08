package net.meilcli.hubber.toolchain.config.android.module

import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

class CoreUiModulePlugin : BasePlugin() {

    override fun apply(project: Project) {
        project.applyPlugin()
        super.apply(project)

        AndroidNamespace.apply(project)
        project.implementationDataModulesOfSameDomain()
    }

    private fun Project.applyPlugin() {
        plugins.apply("com.android.library")
        plugins.apply("org.jetbrains.kotlin.android")
        plugins.apply("org.jetbrains.kotlin.kapt")
        plugins.apply("com.google.dagger.hilt.android")
        plugins.apply("com.google.devtools.ksp")
        plugins.apply("io.gitlab.arturbosch.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.android")
        plugins.apply("net.meilcli.hubber.toolchain.config.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.dagger")
        plugins.apply("net.meilcli.hubber.toolchain.config.ksp")
    }

    override fun DependencyHandler.applyDependencies() {
        setupKotlin()
        setupAndroidUi()
        setupTest()
    }
}
