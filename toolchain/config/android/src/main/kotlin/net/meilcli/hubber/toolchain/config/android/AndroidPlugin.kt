package net.meilcli.hubber.toolchain.config.android

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidPlugin : BasePlugin() {

    companion object {

        private const val compileSdkVersion = 33
        private const val minSdkVersion = 24
        private const val targetSdkVersion = 33
        private const val composeCompiler = "1.4.6"
    }

    override fun apply(project: Project) {
        super.apply(project)

        project.extensions
            .findByType(AppExtension::class.java)
            ?.let { setup(it) }
        project.extensions
            .findByType(LibraryExtension::class.java)
            ?.let { setup(it) }
        project.afterEvaluate {
            // afterEvaluate reason is that other plugin will set JDK version to other(in using kapt and ksp case)
            project.tasks
                .withType(KotlinCompile::class.java)
                .forEach {
                    it.kotlinOptions.jvmTarget = "1.8"
                }
        }
    }

    private fun setup(extension: BaseExtension) {
        extension.compileSdkVersion(compileSdkVersion)
        extension.defaultConfig {
            it.minSdk = minSdkVersion
            it.targetSdk = targetSdkVersion
            it.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            it.consumerProguardFile("consumer-rules.pro")
        }
        extension.buildTypes.getByName("release") {
            it.minifyEnabled(false)
            it.proguardFiles(
                extension.getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        extension.compileOptions {
            it.setSourceCompatibility(JavaVersion.VERSION_1_8)
            it.setTargetCompatibility(JavaVersion.VERSION_1_8)
        }
        extension.testOptions {
            it.unitTests {
                // for robolectric
                isIncludeAndroidResources = true
            }
        }

        setupCompose(extension)
    }

    @Suppress("UnstableApiUsage")
    private fun setupCompose(extension: BaseExtension) {
        extension.buildFeatures.compose = true
        extension.composeOptions.kotlinCompilerExtensionVersion = composeCompiler
    }
}
