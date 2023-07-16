package net.meilcli.hubber.toolchain.config.android.module

import com.google.devtools.ksp.gradle.KspExtension
import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.configurationcache.extensions.capitalized

abstract class BaseContractModulePlugin : BasePlugin() {

    override fun apply(project: Project) {
        project.applyPlugin()
        super.apply(project)

        AndroidNamespace.apply(project)
        project.applyNavigationDestinations()
    }

    private fun Project.applyPlugin() {
        plugins.apply("com.android.library")
        plugins.apply("org.jetbrains.kotlin.android")
        plugins.apply("org.jetbrains.kotlin.kapt")
        plugins.apply("org.jetbrains.kotlin.plugin.parcelize")
        plugins.apply("com.google.dagger.hilt.android")
        plugins.apply("com.google.devtools.ksp")
        plugins.apply("io.gitlab.arturbosch.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.android")
        plugins.apply("net.meilcli.hubber.toolchain.config.detekt")
        plugins.apply("net.meilcli.hubber.toolchain.config.dagger")
        plugins.apply("net.meilcli.hubber.toolchain.config.ksp")
    }

    private fun Project.applyNavigationDestinations() {
        val moduleName = project.projectDir.name
        val domainName = project.projectDir.parentFile.parentFile.name
        extensions.findByType(KspExtension::class.java)
            ?.also {
                it.arg("compose-destinations.mode", "navgraphs")
                it.arg("compose-destinations.moduleName", "${domainName.capitalized()}${moduleName.capitalized()}")
            }
    }

    override fun DependencyHandler.applyDependencies() {
        setupKotlin()
        setupAndroidContract()
        setupTest()
    }
}
