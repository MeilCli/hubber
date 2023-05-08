package net.meilcli.hubber.toolchain.config.android

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import net.meilcli.hubber.toolchain.config.core.BasePlugin
import org.gradle.api.Project
import java.io.File

class KspPlugin : BasePlugin() {

    override fun apply(project: Project) {
        super.apply(project)

        project.extensions.findByType(AppExtension::class.java)
            ?.apply {
                applicationVariants.all { variant ->
                    variant.addJavaSourceFoldersToModel(File(project.buildDir, "generated/ksp/${variant.name}/kotlin"))
                }
            }
        project.extensions.findByType(LibraryExtension::class.java)
            ?.apply {
                libraryVariants.all { variant ->
                    variant.addJavaSourceFoldersToModel(File(project.buildDir, "generated/ksp/${variant.name}/kotlin"))
                }
            }
    }
}
