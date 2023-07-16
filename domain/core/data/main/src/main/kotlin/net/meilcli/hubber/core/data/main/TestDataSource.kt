package net.meilcli.hubber.core.data.main

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.meilcli.hubber.core.data.main.internal.memory.TestMemoryStorage
import net.meilcli.hubber.core.data.source.memory.IMemoryStorageContainer
import javax.inject.Singleton

interface ITestDataSource {

    suspend fun updateMessage(message: String)

    suspend fun getMessage(): String
}

@Module
@InstallIn(SingletonComponent::class)
object TestDataSourceModule {

    @Provides
    @Singleton
    fun provideTestDataSource(
        memoryStorageContainer: IMemoryStorageContainer
    ): ITestDataSource {
        return TestMemoryStorage(memoryStorageContainer)
    }
}
