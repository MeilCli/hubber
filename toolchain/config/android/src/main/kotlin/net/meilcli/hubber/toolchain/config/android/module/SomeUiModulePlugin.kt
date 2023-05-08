package net.meilcli.hubber.toolchain.config.android.module

import org.gradle.api.Project

class SomeUiModulePlugin : BaseUiModulePlugin() {

    override fun apply(project: Project) {
        super.apply(project)

        project.implementationDataModulesOfCoreDomain()
        project.implementationUiModulesOfCoreDomain()
    }
}
