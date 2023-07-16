package net.meilcli.hubber.toolchain.config.android.module

import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

abstract class BaseUiModulePlugin : BasePlugin() {

    override fun apply(project: Project) {
        project.applyPlugin()
        super.apply(project)

        AndroidNamespace.apply(project)
        project.implementationDataModulesOfSameDomain()
        project.implementationContractModulesOfAllDomain()
    }

    private fun Project.applyPlugin() {
        plugins.apply("com.android.library")
        plugins.apply("org.jetbrains.kotlin.android")
        plugins.apply("org.jetbrains.kotlin.kapt")
        plugins.apply("org.jetbrains.kotlin.plugin.parcelize")
        plugins.apply("com.google.dagger.hilt.android")
        plugins.apply("io.gitlab.arturbosch.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.android")
        plugins.apply("net.meilcli.hubber.toolchain.config.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.dagger")
    }

    override fun DependencyHandler.applyDependencies() {
        setupKotlin()
        setupAndroidUi()
        setupTest()
    }
}
