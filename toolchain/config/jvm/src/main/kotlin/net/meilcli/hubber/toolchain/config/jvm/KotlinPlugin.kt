package net.meilcli.hubber.toolchain.config.jvm

import net.meilcli.hubber.config.Dependencies
import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.artifacts.dsl.DependencyHandler

class KotlinPlugin : BasePlugin() {

    override fun DependencyHandler.applyDependencies() {
        superApplyDependencies()

        testImplementation(Dependencies.Junit.Junit)
        testImplementation(Dependencies.IoMockk.Mockk)
    }
}
