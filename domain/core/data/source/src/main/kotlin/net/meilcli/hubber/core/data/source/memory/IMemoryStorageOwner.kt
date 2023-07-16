package net.meilcli.hubber.core.data.source.memory

import net.meilcli.hubber.core.data.source.Lifespan

interface IMemoryStorageOwner {

    fun IMemoryStorageContainer.lifeSpan(lifespan: Lifespan): IMemoryStorage {
        return lifeSpan(lifespan, this@IMemoryStorageOwner)
    }
}
