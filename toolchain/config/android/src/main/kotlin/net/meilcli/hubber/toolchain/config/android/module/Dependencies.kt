package net.meilcli.hubber.toolchain.config.android.module

import net.meilcli.hubber.config.Dependencies
import net.meilcli.hubber.toolchain.config.core.androidTestImplementation
import net.meilcli.hubber.toolchain.config.core.debugImplementation
import net.meilcli.hubber.toolchain.config.core.implementation
import net.meilcli.hubber.toolchain.config.core.ksp
import net.meilcli.hubber.toolchain.config.core.testImplementation
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.io.File

fun DependencyHandler.setupKotlin() {
    implementation(platform(Dependencies.OrgJetbrainsKotlin.KotlinBom))
    implementation(Dependencies.OrgJetbrainsKotlin.KotlinStdlibJdk8)
}

fun DependencyHandler.setupAndroidData() {
    implementation(platform(Dependencies.AndroidxCompose.ComposeBom))
    implementation(Dependencies.AndroidxComposeRuntime.Runtime)
}

fun DependencyHandler.setupAndroidContract() {
    implementation(platform(Dependencies.AndroidxCompose.ComposeBom))
    implementation(Dependencies.AndroidxComposeMaterial3.Material3)
    implementation(Dependencies.AndroidxComposeFoundation.Foundation)
    implementation(Dependencies.AndroidxComposeUi.Ui)
    implementation(Dependencies.AndroidxNavigation.NavigationCompose)

    implementation(Dependencies.IoGithubRaamcostaComposeDestinations.Core)
    ksp(Dependencies.IoGithubRaamcostaComposeDestinations.Ksp)
}

fun DependencyHandler.setupAndroidUi() {
    implementation(Dependencies.AndroidxAppcompat.Appcompat)
    implementation(Dependencies.AndroidxActivity.ActivityCompose)
    implementation(Dependencies.AndroidxLifecycle.LifecycleViewmodelCompose)

    implementation(platform(Dependencies.AndroidxCompose.ComposeBom))
    implementation(Dependencies.AndroidxComposeMaterial3.Material3)
    implementation(Dependencies.AndroidxComposeFoundation.Foundation)
    implementation(Dependencies.AndroidxComposeUi.Ui)
    implementation(Dependencies.AndroidxComposeUi.UiToolingPreview)
    implementation(Dependencies.AndroidxNavigation.NavigationCompose)
    debugImplementation(Dependencies.AndroidxComposeUi.UiTooling)
    debugImplementation(Dependencies.AndroidxComposeUi.UiTestManifest)

    implementation(Dependencies.IoGithubRaamcostaComposeDestinations.Core)

    androidTestImplementation(platform(Dependencies.AndroidxCompose.ComposeBom))
    androidTestImplementation(Dependencies.AndroidxComposeUi.UiTestJunit4)
}

fun DependencyHandler.setupTest() {
    testImplementation(Dependencies.Junit.Junit)

    androidTestImplementation(Dependencies.AndroidxTestExt.Junit)
    androidTestImplementation(Dependencies.AndroidxTestEspresso.EspressoCore)
}

@Suppress("detekt.NestedBlockDepth")
fun Project.implementationAllModulesOfAllDomain() {
    val root = File(rootDir, "domain")
    for (domain in root.listFiles().orEmpty()) {
        for (layer in domain.listFiles().orEmpty()) {
            for (module in layer.listFiles().orEmpty()) {
                val buildGradle = File(module, "build.gradle")
                if (buildGradle.exists().not()) {
                    continue
                }
                val projectPath = ":domain:${domain.name}:${layer.name}:${module.name}"
                dependencies.implementation(project(projectPath))
            }
        }
    }
}

@Suppress("detekt.NestedBlockDepth")
fun Project.implementationContractModulesOfAllDomain() {
    val root = File(rootDir, "domain")
    for (domain in root.listFiles().orEmpty()) {
        for (layer in domain.listFiles().orEmpty()) {
            if (layer.name != "contract") {
                continue
            }
            for (module in layer.listFiles().orEmpty()) {
                val buildGradle = File(module, "build.gradle")
                if (buildGradle.exists().not()) {
                    continue
                }
                val projectPath = ":domain:${domain.name}:${layer.name}:${module.name}"
                dependencies.implementation(project(projectPath))
            }
        }
    }
}

fun Project.implementationDataModulesOfSameDomain() {
    val dataModules = File(projectDir.parentFile.parentFile, "data")
    for (module in dataModules.listFiles().orEmpty()) {
        val buildGradle = File(module, "build.gradle")
        if (buildGradle.exists().not()) {
            continue
        }
        val layer = module.parentFile
        val domain = layer.parentFile
        val projectPath = ":domain:${domain.name}:${layer.name}:${module.name}"
        dependencies.implementation(project(projectPath))
    }
}

fun Project.implementationDataModulesOfCoreDomain() {
    val dataModules = File(rootDir, "domain/core/data")
    for (module in dataModules.listFiles().orEmpty()) {
        val buildGradle = File(module, "build.gradle")
        if (buildGradle.exists().not()) {
            continue
        }
        val projectPath = ":domain:core:data:${module.name}"
        dependencies.implementation(project(projectPath))
    }
}

fun Project.implementationContractModulesOfCoreDomain() {
    val uiModules = File(rootDir, "domain/core/contract")
    for (module in uiModules.listFiles().orEmpty()) {
        val buildGradle = File(module, "build.gradle")
        if (buildGradle.exists().not()) {
            continue
        }
        val projectPath = ":domain:core:contract:${module.name}"
        dependencies.implementation(project(projectPath))
    }
}

fun Project.implementationUiModulesOfCoreDomain() {
    val uiModules = File(rootDir, "domain/core/ui")
    for (module in uiModules.listFiles().orEmpty()) {
        val buildGradle = File(module, "build.gradle")
        if (buildGradle.exists().not()) {
            continue
        }
        val projectPath = ":domain:core:ui:${module.name}"
        dependencies.implementation(project(projectPath))
    }
}
