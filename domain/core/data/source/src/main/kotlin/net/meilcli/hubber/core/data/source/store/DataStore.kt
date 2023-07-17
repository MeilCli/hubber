package net.meilcli.hubber.core.data.source.store

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import kotlinx.serialization.KSerializer
import java.util.concurrent.ConcurrentHashMap

internal class DataStore(
    private val context: Context,
    private val fileNamePrefix: String
) : IDataStore {

    private val data = ConcurrentHashMap<String, Data<*>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> data(key: String, defaultValue: T, serializer: KSerializer<T>): IData<T> {
        return data.getOrPut(key) {
            Data(
                jetpackDataStore = DataStoreFactory.create(
                    serializer = JsonSerializer(defaultValue, serializer),
                    produceFile = { context.dataStoreFile("$fileNamePrefix-$key.json") }
                ),
                defaultValue = defaultValue
            )
        } as IData<T>
    }

    suspend fun clear() {
        data.forEach {
            it.value.clearData()
        }
    }
}
