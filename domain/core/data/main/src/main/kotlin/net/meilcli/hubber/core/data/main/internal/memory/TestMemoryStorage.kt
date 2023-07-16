package net.meilcli.hubber.core.data.main.internal.memory

import net.meilcli.hubber.core.data.main.ITestDataSource
import net.meilcli.hubber.core.data.source.Lifespan
import net.meilcli.hubber.core.data.source.memory.IMemoryStorageContainer
import net.meilcli.hubber.core.data.source.memory.IMemoryStorageOwner
import net.meilcli.hubber.core.data.source.memory.memory
import javax.inject.Inject

internal class TestMemoryStorage @Inject constructor(
    memoryStorageContainer: IMemoryStorageContainer
) : ITestDataSource, IMemoryStorageOwner {

    private val memory = memoryStorageContainer.lifeSpan(Lifespan.Forever).memory("hello")

    override suspend fun updateMessage(message: String) {
        memory.updateData { message }
    }

    override suspend fun getMessage(): String {
        return memory.getData()
    }
}
