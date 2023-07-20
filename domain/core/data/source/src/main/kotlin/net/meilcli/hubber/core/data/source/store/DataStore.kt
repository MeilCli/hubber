package net.meilcli.hubber.core.data.source.store

import androidx.datastore.core.DataStoreFactory
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
    private val data = ConcurrentHashMap<String, Data<*>>()

    // This scope is DataStoreFactory's default value. Usage is release file lock
    private var scope = createNewScope()

    private fun createNewScope(): CoroutineScope {
        return CoroutineScope(coroutineDispatcherProvider.provideIoDispatcher() + SupervisorJob())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> data(key: String, defaultValue: T, serializer: KSerializer<T>): IData<T> {
        return data.getOrPut(key) {
            Data(
                jetpackDataStoreCreator = {
                    // acquire file lock
                    DataStoreFactory.create(
                        serializer = JsonSerializer(defaultValue, serializer),
                        produceFile = { File(parentDirectory, "$key.json") },
                        scope = scope
                    )
                }
            )
        } as IData<T>
    }

    suspend fun clear() {
        data.forEach {
            it.value.acquireJetpackDataStoreLock()
        }
        try {
            // release file lock
            scope.cancel()
            parentDirectory.deleteRecursively()
            scope = createNewScope()
            data.forEach {
                // cancelled DataStore cannot use since here, so recreate
                it.value.recreateJetpackDataStore()
            }
        } finally {
            data.forEach {
                it.value.releaseJetpackDataStoreLock()
            }
        }
    }
}
