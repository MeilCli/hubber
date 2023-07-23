package net.meilcli.hubber.core.data.source.store

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class EntityData<T>(
    private val jetpackDataStoreCreator: () -> JetpackDataStore<T>
) : IData<T> {

    private val mutex = Mutex()
    private var jetpackDataStore = jetpackDataStoreCreator()

    override suspend fun updateData(transform: suspend (T) -> T) {
        mutex.withLock {
            jetpackDataStore.updateData(transform)
        }
    }

    override suspend fun getData(): T {
        return mutex.withLock {
            jetpackDataStore.data.first()
        }
    }

    suspend fun acquireJetpackDataStoreLock() {
        mutex.lock()
    }

    fun releaseJetpackDataStoreLock() {
        mutex.unlock()
    }

    fun recreateJetpackDataStore() {
        jetpackDataStore = jetpackDataStoreCreator()
    }
}
