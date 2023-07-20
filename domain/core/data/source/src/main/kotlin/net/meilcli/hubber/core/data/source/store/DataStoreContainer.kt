package net.meilcli.hubber.core.data.source.store

import android.content.Context
import net.meilcli.hubber.core.data.source.DefaultCoroutineDispatcherProvider
import net.meilcli.hubber.core.data.source.DefaultLocalFileDirectoryProvider
import net.meilcli.hubber.core.data.source.ICoroutineDispatcherProvider
import net.meilcli.hubber.core.data.source.ILocalFileDirectoryProvider
import net.meilcli.hubber.core.data.source.Lifespan
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner

internal class DataStoreContainer(
    localFileDirectoryProvider: ILocalFileDirectoryProvider,
    coroutineDispatcherProvider: ICoroutineDispatcherProvider
) : IDataStoreContainer, ILifespanCleaner {

    private val foreverDataStore = DataStore(
        localFileDirectoryProvider = localFileDirectoryProvider,
        coroutineDispatcherProvider = coroutineDispatcherProvider,
        parentDirectoryName = "forever"
    )
    private val untilLogoutDataStore = DataStore(
        localFileDirectoryProvider = localFileDirectoryProvider,
        coroutineDispatcherProvider = coroutineDispatcherProvider,
        parentDirectoryName = "until_logout"
    )
    private val untilTaskKillDataStore = DataStore(
        localFileDirectoryProvider = localFileDirectoryProvider,
        coroutineDispatcherProvider = coroutineDispatcherProvider,
        parentDirectoryName = "until_task_kill"
    )

    constructor(context: Context) : this(DefaultLocalFileDirectoryProvider(context), DefaultCoroutineDispatcherProvider())

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
