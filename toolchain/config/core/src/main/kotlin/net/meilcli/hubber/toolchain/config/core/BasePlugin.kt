package net.meilcli.hubber.toolchain.config.core

import net.meilcli.hubber.config.Dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler

@Suppress("detekt.UnnecessaryAbstractClass")
abstract class BasePlugin : Plugin<Project> {

    protected fun DependencyHandler.implementation(dependency: String) {
        add("implementation", dependency)
    }

    protected fun DependencyHandler.debugImplementation(dependency: String) {
        add("debugImplementation", dependency)
    }

    protected fun DependencyHandler.testImplementation(dependency: String) {
        add("testImplementation", dependency)
    }

    protected fun DependencyHandler.androidTestImplementation(dependency: String) {
        add("androidTestImplementation", dependency)
    }

    protected fun DependencyHandler.kapt(dependency: String) {
        add("kapt", dependency)
    }

    override fun apply(project: Project) {
        project.repositories.applyRepositories()
        project.dependencies.applyDependencies()
    }

    protected fun RepositoryHandler.superApplyRepositories() {
        mavenCentral()
    }

    open fun RepositoryHandler.applyRepositories() {
        superApplyRepositories()
    }

    protected fun DependencyHandler.superApplyDependencies() {
        add("implementation", platform(Dependencies.OrgJetbrainsKotlin.KotlinBom))
        implementation(Dependencies.OrgJetbrainsKotlin.KotlinStdlibJdk8)
    }

    open fun DependencyHandler.applyDependencies() {
        superApplyDependencies()
    }
}
