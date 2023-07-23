package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

internal class PreferenceData(
    private val jetpackDataStoreCreator: () -> JetpackDataStore<Preferences>
) : IPreferenceData {

    private val mutex = Mutex()
    private var jetpackDataStore = jetpackDataStoreCreator()

    private val pickPreferenceData = ConcurrentHashMap<Preferences.Key<*>, PickPreferenceData<*>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> pick(key: Preferences.Key<T>, defaultValue: T): IData<T> {
        return pickPreferenceData.getOrPut(key) {
            PickPreferenceData(this, key, defaultValue)
        } as IData<T>
    }

    suspend fun <T> updateData(key: Preferences.Key<T>, defaultValue: T, transform: suspend (T) -> T) {
        mutex.withLock {
            jetpackDataStore.edit {
                val value = it[key] ?: defaultValue
                it[key] = transform(value)
            }
        }
    }

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences) {
        mutex.withLock {
            jetpackDataStore.updateData(transform)
        }
    }

    suspend fun <T> getData(key: Preferences.Key<T>, defaultValue: T): T {
        return mutex.withLock {
            jetpackDataStore.data
                .map { it[key] ?: defaultValue }
                .first()
        }
    }

    override suspend fun getData(): Preferences {
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
