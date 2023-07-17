package net.meilcli.hubber.core.data.source.store

import android.content.Context
import net.meilcli.hubber.core.data.source.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan

internal class DataStoreContainer(
    context: Context
) : IDataStoreContainer, ILifespanCleaner {

    private val foreverMemory = DataStore(context = context, fileNamePrefix = "forever")
    private val untilLogoutMemory = DataStore(context = context, fileNamePrefix = "until_logout")
    private val untilTaskKillMemory = DataStore(context = context, fileNamePrefix = "until_task_kill")

    override fun lifeSpan(lifespan: Lifespan): IDataStore {
        return when (lifespan) {
            Lifespan.Forever -> foreverMemory
            Lifespan.UntilLogout -> untilLogoutMemory
            Lifespan.UntilTaskKill -> untilTaskKillMemory
        }
    }

    override suspend fun clearByLogout() {
        untilLogoutMemory.clear()
    }

    override suspend fun clearByTaskKill() {
        untilTaskKillMemory.clear()
    }
}
