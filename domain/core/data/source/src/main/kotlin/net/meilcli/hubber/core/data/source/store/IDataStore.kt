package net.meilcli.hubber.core.data.source.store

import kotlinx.serialization.KSerializer

interface IDataStore {

    fun <T> data(key: String, defaultValue: T, serializer: KSerializer<T>): IData<T>
}
