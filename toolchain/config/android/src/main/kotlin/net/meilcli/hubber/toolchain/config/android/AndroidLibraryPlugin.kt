package net.meilcli.hubber.toolchain.config.android

import com.android.build.gradle.LibraryExtension
import net.meilcli.hubber.config.Dependencies
import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryPlugin : BasePlugin() {

    override fun apply(project: Project) {
        super.apply(project)

        project.extensions
            .findByType(LibraryExtension::class.java)
            ?.let { BaseExtensionApplier.apply(it) }
        project.extensions
            .findByType(KotlinProjectExtension::class.java)
            ?.jvmToolchain(8)
        project.tasks
            .withType(KotlinCompile::class.java)
            .forEach {
                it.kotlinOptions.jvmTarget = "1.8"
            }
    }

    override fun RepositoryHandler.applyRepositories() {
        superApplyRepositories()

        google()
    }

    override fun DependencyHandler.applyDependencies() {
        superApplyDependencies()

        implementation(Dependencies.AndroidxAppcompat.Appcompat)
        implementation(Dependencies.AndroidxActivity.ActivityCompose)
        implementation(Dependencies.AndroidxLifecycle.LifecycleViewmodelCompose)

        add("implementation", platform(Dependencies.AndroidxCompose.ComposeBom))
        implementation(Dependencies.AndroidxComposeMaterial3.Material3)
        implementation(Dependencies.AndroidxComposeFoundation.Foundation)
        implementation(Dependencies.AndroidxComposeUi.Ui)
        implementation(Dependencies.AndroidxComposeUi.UiToolingPreview)
        debugImplementation(Dependencies.AndroidxComposeUi.UiTooling)
        debugImplementation(Dependencies.AndroidxComposeUi.UiTestManifest)

        testImplementation(Dependencies.Junit.Junit)

        androidTestImplementation(Dependencies.AndroidxTestExt.Junit)
        androidTestImplementation(Dependencies.AndroidxTestEspresso.EspressoCore)

        add("androidTestImplementation", platform(Dependencies.AndroidxCompose.ComposeBom))
        androidTestImplementation(Dependencies.AndroidxComposeUi.UiTestJunit4)
    }
}
