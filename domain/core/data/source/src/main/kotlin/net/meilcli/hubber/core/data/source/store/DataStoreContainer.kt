package net.meilcli.hubber.core.data.source.store

import android.content.Context
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan

internal class DataStoreContainer(
    context: Context
) : IDataStoreContainer, ILifespanCleaner {

    private val foreverDataStore = DataStore(context = context, parentDirectoryName = "forever")
    private val untilLogoutDataStore = DataStore(context = context, parentDirectoryName = "until_logout")
    private val untilTaskKillDataStore = DataStore(context = context, parentDirectoryName = "until_task_kill")

    override fun lifeSpan(lifespan: Lifespan): IDataStore {
        return when (lifespan) {
            Lifespan.Forever -> foreverDataStore
            Lifespan.UntilLogout -> untilLogoutDataStore
            Lifespan.UntilTaskKill -> untilTaskKillDataStore
        }
    }

    override suspend fun clearByLogout() {
        untilLogoutDataStore.clear()
    }

    override suspend fun clearByTaskKill() {
        untilTaskKillDataStore.clear()
    }
}
