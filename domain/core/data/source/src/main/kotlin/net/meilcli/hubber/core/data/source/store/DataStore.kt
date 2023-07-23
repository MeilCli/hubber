package net.meilcli.hubber.core.data.source.store

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.serialization.KSerializer
import net.meilcli.hubber.core.data.source.ICoroutineDispatcherProvider
import net.meilcli.hubber.core.data.source.ILocalFileDirectoryProvider
import java.io.File
import java.util.concurrent.ConcurrentHashMap

internal class DataStore(
    localFileDirectoryProvider: ILocalFileDirectoryProvider,
    private val coroutineDispatcherProvider: ICoroutineDispatcherProvider,
    parentDirectoryName: String
) : IDataStore {

    private val parentDirectory = File(localFileDirectoryProvider.provideFileDirectory(), "dataStore/$parentDirectoryName")
    private val entityData = ConcurrentHashMap<String, EntityData<*>>()
    private val preferenceData = ConcurrentHashMap<String, PreferenceData>()

    // This scope is DataStoreFactory's default value. Usage is release file lock
    private var scope = createNewScope()

    private fun createNewScope(): CoroutineScope {
        return CoroutineScope(coroutineDispatcherProvider.provideIoDispatcher() + SupervisorJob())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> entityData(
        key: String,
        defaultValue: T,
        serializer: KSerializer<T>,
        dataMigrations: List<DataMigration<T>>
    ): IData<T> {
        return entityData.getOrPut(key) {
            EntityData(
                jetpackDataStoreCreator = {
                    // acquire file lock
                    DataStoreFactory.create(
                        serializer = JsonSerializer(defaultValue, serializer),
                        produceFile = { File(parentDirectory, "$key.json") },
                        scope = scope,
                        migrations = dataMigrations
                    )
                }
            )
        } as IData<T>
    }

    override fun preferenceData(name: String, dataMigrations: List<DataMigration<Preferences>>): IPreferenceData {
        return preferenceData.getOrPut(name) {
            PreferenceData(
                jetpackDataStoreCreator = {
                    // acquire file lock
                    PreferenceDataStoreFactory.create(
                        produceFile = { File(parentDirectory, "$name.preferences_pb") },
                        scope = scope,
                        migrations = dataMigrations
                    )
                }
            )
        }
    }

    suspend fun clear() {
        entityData.forEach {
            it.value.acquireJetpackDataStoreLock()
        }
        preferenceData.forEach {
            it.value.acquireJetpackDataStoreLock()
        }
        try {
            // release file lock
            scope.cancel()
            parentDirectory.deleteRecursively()
            scope = createNewScope()

            // cancelled DataStore cannot use since here, so recreate
            entityData.forEach {
                it.value.recreateJetpackDataStore()
            }
            preferenceData.forEach {
                it.value.recreateJetpackDataStore()
            }
        } finally {
            entityData.forEach {
                it.value.releaseJetpackDataStoreLock()
            }
            preferenceData.forEach {
                it.value.releaseJetpackDataStoreLock()
            }
        }
    }
}
