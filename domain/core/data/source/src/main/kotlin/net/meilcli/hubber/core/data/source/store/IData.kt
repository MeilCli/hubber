package net.meilcli.hubber.core.data.source.store

interface IData<T> {

    suspend fun updateData(transform: suspend (T) -> T)

    suspend fun getData(): T
}
