package net.meilcli.hubber.core.data.source.file

import net.meilcli.hubber.core.data.source.ILocalFileDirectoryProvider
import java.io.File

internal class FileSystem(
    localFileDirectoryProvider: ILocalFileDirectoryProvider,
    parentDirectoryName: String
) : IFileSystem {

    private val parentDirectory = File(localFileDirectoryProvider.provideFileDirectory(), "fileSystem/$parentDirectoryName")

    override fun file(fileName: String): File {
        return File(parentDirectory, fileName)
    }

    fun clear() {
        if (parentDirectory.exists()) {
            parentDirectory.deleteRecursively()
        }
    }
}
