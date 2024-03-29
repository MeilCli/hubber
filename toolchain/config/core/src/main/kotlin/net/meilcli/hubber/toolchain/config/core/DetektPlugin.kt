package net.meilcli.hubber.toolchain.config.core

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import net.meilcli.hubber.config.Dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.tasks.TaskProvider
import java.io.File

class DetektPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions
            .findByType(DetektExtension::class.java)
            ?.apply {
                val directory = project.findDetektYmlDirectory()
                buildUponDefaultConfig = true
                config = project.files(File(directory, "detekt.yml"))
                basePath = directory.absolutePath
            }
        val dependencies = mutableListOf<Dependency?>()
        dependencies += project.dependencies.add("detektPlugins", Dependencies.IoGitlabArturboschDetekt.DetektFormatting)
        dependencies += project.dependencies.add("detektPlugins", Dependencies.ToolchainDetektRule.ToolchainDetektRule)
        dependencies.filterIsInstance<ModuleDependency>()
            .forEach { moduleDependency ->
                moduleDependency.exclude(mapOf("group" to "org.slf4j"))
            }
        project.afterEvaluate {
            project.tasks
                .named("check")
                .configure { task ->
                    task.setDependsOn(task.dependsOn.filterNot { depends -> depends is TaskProvider<*> && depends.name == "detekt" })
                }
        }
    }

    private fun Project.findDetektYmlDirectory(): File {
        var current = buildDir
        while (current.parentFile != null) {
            if (File(current, "detekt.yml").exists()) {
                return current
            }
            current = current.parentFile
        }
        return current
    }
}
