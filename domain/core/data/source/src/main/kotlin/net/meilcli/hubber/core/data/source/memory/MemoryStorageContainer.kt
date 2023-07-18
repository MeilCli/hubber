package net.meilcli.hubber.core.data.source.memory

import net.meilcli.hubber.core.data.source.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan
import java.util.concurrent.ConcurrentHashMap

internal class MemoryStorageContainer : IMemoryStorageContainer, ILifespanCleaner {

    private val foreverMemoryStorage = ConcurrentHashMap<IMemoryStorageOwner, MemoryStorage>()
    private val untilLogoutMemoryStorage = ConcurrentHashMap<IMemoryStorageOwner, MemoryStorage>()
    private val untilTaskKillMemoryStorage = ConcurrentHashMap<IMemoryStorageOwner, MemoryStorage>()

    override fun lifeSpan(lifespan: Lifespan, owner: IMemoryStorageOwner): IMemoryStorage {
        return when (lifespan) {
            Lifespan.Forever -> foreverMemoryStorage.getOrPut(owner) { MemoryStorage() }
            Lifespan.UntilLogout -> untilLogoutMemoryStorage.getOrPut(owner) { MemoryStorage() }
            Lifespan.UntilTaskKill -> untilTaskKillMemoryStorage.getOrPut(owner) { MemoryStorage() }
        }
    }

    override suspend fun clearByLogout() {
        untilLogoutMemoryStorage.forEach {
            it.value.clear()
        }
    }

    override suspend fun clearByTaskKill() {
        untilTaskKillMemoryStorage.forEach {
            it.value.clear()
        }
    }
}
