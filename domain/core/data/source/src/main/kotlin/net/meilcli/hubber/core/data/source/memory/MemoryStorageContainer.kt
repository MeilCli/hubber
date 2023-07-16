package net.meilcli.hubber.core.data.source.memory

import net.meilcli.hubber.core.data.source.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan
import java.util.concurrent.ConcurrentHashMap

internal class MemoryStorageContainer : IMemoryStorageContainer, ILifespanCleaner {

    private val foreverMemory = ConcurrentHashMap<IMemoryStorageOwner, MemoryStorage>()
    private val untilLogoutMemory = ConcurrentHashMap<IMemoryStorageOwner, MemoryStorage>()
    private val untilTaskKillMemory = ConcurrentHashMap<IMemoryStorageOwner, MemoryStorage>()

    override fun lifeSpan(lifespan: Lifespan, owner: IMemoryStorageOwner): IMemoryStorage {
        return when (lifespan) {
            Lifespan.Forever -> foreverMemory.getOrPut(owner) { MemoryStorage() }
            Lifespan.UntilLogout -> untilLogoutMemory.getOrPut(owner) { MemoryStorage() }
            Lifespan.UntilTaskKill -> untilTaskKillMemory.getOrPut(owner) { MemoryStorage() }
        }
    }

    override suspend fun clearByLogout() {
        untilLogoutMemory.forEach {
            it.value.clear()
        }
    }

    override suspend fun clearByTaskKill() {
        untilTaskKillMemory.forEach {
            it.value.clear()
        }
    }
}
