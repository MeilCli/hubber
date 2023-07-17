package net.meilcli.hubber.toolchain.config.android.module

import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

class AppModulePlugin : BasePlugin() {

    override fun apply(project: Project) {
        project.applyPlugin()
        super.apply(project)

        project.implementationAllModulesOfAllDomain()
    }

    private fun Project.applyPlugin() {
        plugins.apply("com.android.application")
        plugins.apply("org.jetbrains.kotlin.android")
        plugins.apply("org.jetbrains.kotlin.kapt")
        plugins.apply("org.jetbrains.dokka")
        plugins.apply("com.google.dagger.hilt.android")
        plugins.apply("com.google.devtools.ksp")
        plugins.apply("io.gitlab.arturbosch.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.dokka")
        plugins.apply("net.meilcli.hubber.toolchain.config.android")
        plugins.apply("net.meilcli.hubber.toolchain.config.dagger")
        plugins.apply("net.meilcli.hubber.toolchain.config.ksp")
    }

    override fun DependencyHandler.applyDependencies() {
        setupKotlin()
        setupAndroidData()
        setupAndroidContract()
        setupAndroidUi()
        setupTest()
    }
}
