package net.meilcli.hubber.core.data.source.file

import android.content.Context
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan

internal class FileSystemContainer(
    context: Context
) : IFileSystemContainer, ILifespanCleaner {

    private val foreverFileSystem = FileSystem(context = context, parentDirectoryName = "forever")
    private val untilLogoutSystem = FileSystem(context = context, parentDirectoryName = "until_logout")
    private val untilTaskKillSystem = FileSystem(context = context, parentDirectoryName = "until_task_kill")

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
