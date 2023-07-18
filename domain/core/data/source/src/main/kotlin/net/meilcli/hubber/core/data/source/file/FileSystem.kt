package net.meilcli.hubber.core.data.source.file

import android.content.Context
import java.io.File

internal class FileSystem(
    context: Context,
    parentDirectoryName: String
) : IFileSystem {

    private val parentDirectory = File(context.filesDir, "fileSystem/$parentDirectoryName")

    override fun file(fileName: String): File {
        return File(parentDirectory, fileName)
    }

    fun clear() {
        if (parentDirectory.exists()) {
            parentDirectory.deleteRecursively()
        }
    }
}
