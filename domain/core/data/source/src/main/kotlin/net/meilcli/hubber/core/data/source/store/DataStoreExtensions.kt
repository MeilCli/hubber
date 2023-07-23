package net.meilcli.hubber.core.data.source.store

import kotlinx.serialization.serializer

inline fun <reified T> IDataStore.entityData(key: String, defaultValue: T): IData<T> {
    return entityData(key, defaultValue, serializer())
}
