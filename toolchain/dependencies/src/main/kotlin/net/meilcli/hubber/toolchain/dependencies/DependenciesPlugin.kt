package net.meilcli.hubber.toolchain.dependencies

import net.meilcli.hubber.config.Dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import java.io.File

class DependenciesPlugin : Plugin<Project> {

    companion object {

        private const val fileName = "Dependencies.kt"
        private const val extensionName = "hubberDependencies"
    }

    override fun apply(project: Project) {
        project.extensions.create(extensionName, DependenciesExtension::class.java)
        project.afterEvaluate {
            project.extensions
                .findByType(DependenciesExtension::class.java)
                ?.apply {
                    if (includeSourceSets) {
                        val outputDirectoryPath = "${project.buildDir}/generated/source/dependencies/main/net/meilcli/hubber/config"
                        val outputDirectory = project.file(outputDirectoryPath)
                        if (outputDirectory.exists().not()) {
                            outputDirectory.mkdirs()
                        }
                        val fromDirectory = File(Dependencies.sourceSetsPath)
                        File(fromDirectory, fileName).copyTo(File(outputDirectory, fileName), overwrite = true)

                        project.configureOutputDirectoryAsSourceSets(outputDirectory)
                    }
                }
        }
    }

    private fun Project.configureOutputDirectoryAsSourceSets(outputDirectory: File) {
        extensions
            .findByType(KotlinProjectExtension::class.java)
            ?.apply {
                sourceSets.findByName("main")?.kotlin?.srcDir(outputDirectory)
            }
    }
}
