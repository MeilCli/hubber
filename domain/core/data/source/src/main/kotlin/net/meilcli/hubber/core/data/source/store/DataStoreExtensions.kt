package net.meilcli.hubber.core.data.source.store

import androidx.datastore.core.DataMigration
import kotlinx.serialization.serializer

inline fun <reified T> IDataStore.entityData(
    key: String,
    defaultValue: T,
    dataMigrations: List<DataMigration<T>> = emptyList()
): IData<T> {
    return entityData(key, defaultValue, serializer(), dataMigrations)
}
