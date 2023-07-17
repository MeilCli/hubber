package net.meilcli.hubber.toolchain.config.core

import net.meilcli.hubber.config.Dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTaskPartial

class DokkaPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // avoid implicit dependencies between tasks
        // https://docs.gradle.org/8.1.1/userguide/validation_problems.html#implicit_dependency
        project.tasks
            .withType(DokkaTaskPartial::class.java)
            .configureEach { dokkaTaskPartial ->
                project.tasks
                    .findByName("kaptDebugKotlin")
                    ?.also {
                        dokkaTaskPartial.dependsOn(it)
                    }
                project.tasks
                    .findByName("kaptReleaseKotlin")
                    ?.also {
                        dokkaTaskPartial.dependsOn(it)
                    }
            }
        project.tasks
            .withType(DokkaTaskPartial::class.java)
            .configureEach { dokkaTaskPartial ->
                dokkaTaskPartial.moduleName.set(createModuleName(project))
                dokkaTaskPartial.suppressInheritedMembers.set(true)
            }
        project.dependencies.add("dokkaPlugin", Dependencies.ToolchainDokkaPlugin.ToolchainDokkaPlugin)
    }

    private fun createModuleName(project: Project): String {
        var current: Project? = project
        var result = ""
        while (current != null && current != project.rootProject) {
            result = ":${current.name}" + result
            current = current.parent
        }
        return result
    }
}
