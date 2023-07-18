package net.meilcli.hubber.core.data.source.cleaner

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface ILifespanCleaner {

    suspend fun clearByLogout()

    suspend fun clearByTaskKill()
}

@Module
@InstallIn(SingletonComponent::class)
object LifespanCleanerModule {

    @CompositeLifespanCleaner
    @Provides
    @Singleton
    fun provideLifespanCleaner(
        @MemoryStorageCleaner
        memoryStorageCleaner: ILifespanCleaner,
        @DataStoreCleaner
        dataStoreCleaner: ILifespanCleaner,
        @FileSystemCleaner
        fileSystemCleaner: ILifespanCleaner
    ): ILifespanCleaner {
        return LifespanCleaner(
            memoryStorageCleaner = memoryStorageCleaner,
            dataStoreCleaner = dataStoreCleaner,
            fileSystemCleaner = fileSystemCleaner
        )
    }
}
