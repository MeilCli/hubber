package net.meilcli.hubber.toolchain.config.android

import net.meilcli.hubber.config.Dependencies
import net.meilcli.hubber.toolchain.config.core.BasePlugin
import net.meilcli.hubber.toolchain.config.core.implementation
import net.meilcli.hubber.toolchain.config.core.kapt
import net.meilcli.hubber.toolchain.config.core.kaptTest
import net.meilcli.hubber.toolchain.config.core.testImplementation
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class DaggerPlugin : BasePlugin() {

    override fun apply(project: Project) {
        super.apply(project)

        project.extensions
            .findByType(KaptExtension::class.java)
            ?.correctErrorTypes = true
    }

    override fun DependencyHandler.applyDependencies() {
        implementation(Dependencies.ComGoogleDagger.HiltAndroid)
        kapt(Dependencies.ComGoogleDagger.HiltAndroidCompiler)
        testImplementation(Dependencies.ComGoogleDagger.HiltAndroidTesting)
        kaptTest(Dependencies.ComGoogleDagger.HiltAndroidCompiler)
    }
}
