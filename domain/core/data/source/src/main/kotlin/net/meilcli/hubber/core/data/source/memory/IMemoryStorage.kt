package net.meilcli.hubber.core.data.source.memory

interface IMemoryStorage {

    fun <T> memory(key: String, defaultValue: T): IMemory<T>
}
