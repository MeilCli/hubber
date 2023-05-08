package net.meilcli.hubber.toolchain.config.jvm

import net.meilcli.hubber.config.Dependencies
import net.meilcli.hubber.toolchain.config.core.BasePlugin
import net.meilcli.hubber.toolchain.config.core.testImplementation
import org.gradle.api.artifacts.dsl.DependencyHandler

class KotlinPlugin : BasePlugin() {

    override fun DependencyHandler.applyDependencies() {
        testImplementation(Dependencies.Junit.Junit)
        testImplementation(Dependencies.IoMockk.Mockk)
    }
}
