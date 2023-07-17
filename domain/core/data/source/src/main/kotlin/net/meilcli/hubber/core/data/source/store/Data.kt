package net.meilcli.hubber.core.data.source.store

import kotlinx.coroutines.flow.first

internal class Data<T>(
    private val jetpackDataStore: JetpackDataStore<T>,
    private val defaultValue: T
) : IData<T> {

    override suspend fun updateData(transform: suspend (T) -> T) {
        jetpackDataStore.updateData(transform)
    }

    override suspend fun getData(): T {
        return jetpackDataStore.data.first()
    }

    suspend fun clearData() {
        updateData {
            defaultValue
        }
    }
}
