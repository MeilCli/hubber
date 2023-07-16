package net.meilcli.hubber.core.data.source.memory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.meilcli.hubber.core.data.source.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan
import net.meilcli.hubber.core.data.source.MemoryStorageCleaner
import javax.inject.Singleton

interface IMemoryStorageContainer {

    fun lifeSpan(lifespan: Lifespan, owner: IMemoryStorageOwner): IMemoryStorage
}

@Module
@InstallIn(SingletonComponent::class)
object MemoryStorageContainerModule {

    @Provides
    @Singleton
    fun provideMemoryStorageContainer(): IMemoryStorageContainer {
        return MemoryStorageContainer()
    }

    @MemoryStorageCleaner
    @Provides
    @Singleton
    fun provideMemoryStorageContainerCleaner(
        memoryStorageContainer: IMemoryStorageContainer
    ): ILifespanCleaner {
        return memoryStorageContainer as MemoryStorageContainer
    }
}
