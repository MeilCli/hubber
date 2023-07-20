package net.meilcli.hubber.core.data.source

import android.content.Context
import java.io.File

// This interface is for testing usage
internal interface ILocalFileDirectoryProvider {

    fun provideFileDirectory(): File
}

internal class DefaultLocalFileDirectoryProvider(
    private val context: Context
) : ILocalFileDirectoryProvider {

    override fun provideFileDirectory(): File {
        return context.filesDir
    }
}
