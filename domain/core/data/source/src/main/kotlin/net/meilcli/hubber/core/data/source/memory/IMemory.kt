package net.meilcli.hubber.core.data.source.memory

interface IMemory<T> {

    suspend fun updateData(transform: suspend (T) -> T)

    suspend fun getData(): T
}
