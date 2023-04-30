package net.meilcli.hubber.toolchain.config.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion

object BaseExtensionApplier {

    private const val compileSdkVersion = 33
    private const val minSdkVersion = 24
    private const val targetSdkVersion = 33
    private const val composeCompiler = "1.4.6"

    fun apply(extension: BaseExtension) {
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

        setupCompose(extension)
    }

    @Suppress("UnstableApiUsage")
    private fun setupCompose(extension: BaseExtension) {
        extension.buildFeatures.compose = true
        extension.composeOptions.kotlinCompilerExtensionVersion = composeCompiler
    }
}
