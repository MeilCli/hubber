package net.meilcli.hubber.core.data.source.memory

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class Memory<T>(private val defaultValue: T) : IMemory<T> {

    private val mutex = Mutex()
    private var value = defaultValue

    override suspend fun updateData(transform: suspend (T) -> T) {
        mutex.withLock {
            value = transform(value)
        }
    }

    override suspend fun getData(): T {
        return mutex.withLock { value }
    }

    suspend fun clearData() {
        updateData {
            defaultValue
        }
    }
}
