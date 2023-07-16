package net.meilcli.hubber.core.data.source.memory

inline fun <reified T> IMemoryStorage.memory(defaultValue: T): IMemory<T> {
    return memory(checkNotNull(T::class.java.canonicalName), defaultValue)
}
