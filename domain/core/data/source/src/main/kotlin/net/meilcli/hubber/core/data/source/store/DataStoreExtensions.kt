package net.meilcli.hubber.core.data.source.store

import kotlinx.serialization.serializer

inline fun <reified T> IDataStore.data(key: String, defaultValue: T): IData<T> {
    return data(key, defaultValue, serializer())
}
