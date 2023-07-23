package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class PreferenceDataSource(
    private val jetpackDataStoreCreator: () -> JetpackDataStore<Preferences>
) {

    private val mutex = Mutex()
    private var jetpackDataStore = jetpackDataStoreCreator()

    suspend fun <T> updateData(key: Preferences.Key<T>, defaultValue: T, transform: suspend (T) -> T) {
        mutex.withLock {
            jetpackDataStore.edit {
                val value = it[key] ?: defaultValue
                it[key] = transform(value)
            }
        }
    }

    suspend fun <T> getData(key: Preferences.Key<T>, defaultValue: T): T {
        return mutex.withLock {
            jetpackDataStore.data
                .map { it[key] ?: defaultValue }
                .first()
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
