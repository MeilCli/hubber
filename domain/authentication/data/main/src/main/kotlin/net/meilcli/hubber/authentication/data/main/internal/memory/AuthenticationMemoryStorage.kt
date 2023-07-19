package net.meilcli.hubber.authentication.data.main.internal.memory

import net.meilcli.hubber.authentication.data.main.entity.Authentication
import net.meilcli.hubber.core.data.source.Lifespan
import net.meilcli.hubber.core.data.source.memory.IMemoryStorageContainer
import net.meilcli.hubber.core.data.source.memory.IMemoryStorageOwner
import net.meilcli.hubber.core.data.source.memory.memory

class AuthenticationMemoryStorage(
    memoryStorageContainer: IMemoryStorageContainer
) : IMemoryStorageOwner {

    private val memory = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout).memory<Authentication>(Authentication.Never)

    suspend fun getAuthentication(): Authentication {
        return memory.getData()
    }

    suspend fun updateAuthentication(authentication: Authentication) {
        memory.updateData { authentication }
    }
}
