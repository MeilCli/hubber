package net.meilcli.hubber.toolchain.config.android.module

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import java.io.File

object AndroidNamespace {

    private const val baseNamespace = "net.meilcli.hubber"

    fun apply(project: Project) {
        project.extensions
            .findByType(LibraryExtension::class.java)
            ?.also {
                it.namespace = "$baseNamespace${extractAdditionalNamespace(project.projectDir, "")}"
            }
    }

    private tailrec fun extractAdditionalNamespace(file: File, result: String): String {
        return if (file.name != "domain") extractAdditionalNamespace(file.parentFile, ".${file.name}$result") else result
    }
}
