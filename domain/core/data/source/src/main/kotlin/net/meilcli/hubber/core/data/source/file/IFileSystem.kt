package net.meilcli.hubber.core.data.source.file

import java.io.File

interface IFileSystem {

    fun file(fileName: String): File
}
