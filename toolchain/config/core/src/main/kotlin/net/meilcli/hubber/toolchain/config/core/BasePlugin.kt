package net.meilcli.hubber.toolchain.config.core

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler

@Suppress("detekt.UnnecessaryAbstractClass", "detekt.TooManyFunctions")
abstract class BasePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.repositories.applyRepositories()
        project.dependencies.applyDependencies()
    }

    open fun RepositoryHandler.applyRepositories() {
        mavenCentral()
        google()
    }

    open fun DependencyHandler.applyDependencies() {}
}
