package net.meilcli.hubber.core.data.source.file

import android.content.Context
import net.meilcli.hubber.core.data.source.DefaultLocalFileDirectoryProvider
import net.meilcli.hubber.core.data.source.ILocalFileDirectoryProvider
import net.meilcli.hubber.core.data.source.Lifespan
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner

internal class FileSystemContainer(
    localFileDirectoryProvider: ILocalFileDirectoryProvider
) : IFileSystemContainer, ILifespanCleaner {

    private val foreverFileSystem = FileSystem(
        localFileDirectoryProvider = localFileDirectoryProvider,
        parentDirectoryName = "forever"
    )
    private val untilLogoutSystem = FileSystem(
        localFileDirectoryProvider = localFileDirectoryProvider,
        parentDirectoryName = "until_logout"
    )
    private val untilTaskKillSystem = FileSystem(
        localFileDirectoryProvider = localFileDirectoryProvider,
        parentDirectoryName = "until_task_kill"
    )

    constructor(context: Context) : this(DefaultLocalFileDirectoryProvider(context))

    override fun lifeSpan(lifespan: Lifespan): IFileSystem {
        return when (lifespan) {
            Lifespan.Forever -> foreverFileSystem
            Lifespan.UntilLogout -> untilLogoutSystem
            Lifespan.UntilTaskKill -> untilTaskKillSystem
        }
    }

    override suspend fun clearByLogout() {
        untilLogoutSystem.clear()
    }

    override suspend fun clearByTaskKill() {
        untilTaskKillSystem.clear()
    }
}
