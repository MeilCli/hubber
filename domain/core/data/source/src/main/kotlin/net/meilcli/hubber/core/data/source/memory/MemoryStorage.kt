package net.meilcli.hubber.core.data.source.memory

import java.util.concurrent.ConcurrentHashMap

internal class MemoryStorage : IMemoryStorage {

    private val memories = ConcurrentHashMap<String, Memory<*>>()

    override fun <T> memory(key: String, defaultValue: T): IMemory<T> {
        @Suppress("UNCHECKED_CAST")
        return memories.getOrPut(key) {
            Memory(defaultValue)
        } as IMemory<T>
    }

    suspend fun clear() {
        memories.forEach {
            it.value.clearData()
        }
    }
}
